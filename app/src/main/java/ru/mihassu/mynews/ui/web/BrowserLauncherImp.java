package ru.mihassu.mynews.ui.web;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import ru.mihassu.mynews.R;
import ru.mihassu.mynews.ui.web.ArticleActivity;
import ru.mihassu.mynews.ui.web.BrowserLauncher;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

public class BrowserLauncherImp implements BrowserLauncher {

    // Helper для работы с Chrome CustomTabs
    private CustomTabHelper customTabHelper;

    // !!! Требуется контекст Activity
    private Context activityContext;

    public BrowserLauncherImp(CustomTabHelper customTabHelper, Context activityContext) {
        this.customTabHelper = customTabHelper;
        this.activityContext = activityContext;
    }

    @Override
    public void showInBrowser(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        builder.setToolbarColor(ContextCompat.getColor(activityContext, android.R.color.white));
        builder.addDefaultShareMenuItem();
        builder.setStartAnimations(activityContext, android.R.anim.fade_in, android.R.anim.fade_out);
        builder.setExitAnimations(activityContext, android.R.anim.fade_in, android.R.anim.fade_out);
        builder.setShowTitle(true);

        CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();

        Intent intent = anotherCustomTab.intent;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                activityContext,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addMenuItem("Our custom menu", pendingIntent);

        CustomTabsIntent customTabsIntent = builder.build();

        String packageName = customTabHelper.getPackageNameToUse(activityContext, url);

        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activityContext, Uri.parse(url));
        } else {
            Intent intentOpenUri = new Intent(activityContext, ArticleActivity.class);
            intentOpenUri.putExtra(activityContext.getResources().getString(R.string.article_url_key), url);
            activityContext.startActivity(intentOpenUri);
        }
    }
}
