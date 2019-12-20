package ru.mihassu.mynews.ui.web;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsService;

import java.util.ArrayList;
import java.util.List;

public class CustomTabHelper {

    private static String STABLE_PACKAGE = "com.android.chrome";
    private static String BETA_PACKAGE = "com.chrome.beta";
    private static String DEV_PACKAGE = "com.chrome.dev";
    private static String LOCAL_PACKAGE = "com.google.android.apps.chrome";


    private static String packageNameToUse = null;

    /**
     * Определить установленный пакет с поддержкой CustomTabs
     */
    public String getPackageNameToUse(Context context, String url) {

        if (packageNameToUse != null) return packageNameToUse;

        PackageManager pm = context.getPackageManager();

        // Интент для отображения данных
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // Список активити готовых обработать данный интент
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);

        ArrayList<String> packagesSupportingCustomTabs = new ArrayList<>();

        // Проход по всем активити с целью проверить содержит ли их package сервис
        // c action CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
        for (ResolveInfo info : resolvedActivityList) {

            Intent serviceIntent = new Intent();
            serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);

            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        if (packagesSupportingCustomTabs.isEmpty()) {
            packageNameToUse = null;
        } else if (packagesSupportingCustomTabs.size() == 1) {
            packageNameToUse = packagesSupportingCustomTabs.get(0);
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            packageNameToUse = STABLE_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            packageNameToUse = BETA_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            packageNameToUse = DEV_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            packageNameToUse = LOCAL_PACKAGE;
        }

        return packageNameToUse;
    }
}
