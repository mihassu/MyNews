package ru.mihassu.mynews.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import javax.inject.Inject;

import ru.mihassu.mynews.App;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.di.modules.ui.SettingsFragmentModule;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Inject
    SharedPreferences preferences;

    @Inject Context context;

    private Handler handler;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        App
                .get()
                .getAppComponent()
                .plusSettingsFragmentComponent(new SettingsFragmentModule())
                .inject(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setThemePreference();
        setBottomNavigationMenu();
    }

    private void setThemePreference() {
        Preference.OnPreferenceChangeListener preferenceChangeListener = (preference, value) -> {
            handler = new Handler();
            handler.postDelayed(recreateActivity, 600);
            return true;
        };

        String prefKeyTheme = context.getString(R.string.pref_key_dark_theme);
        Preference themePreference = findPreference(prefKeyTheme);

        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(preferenceChangeListener);
        }
    }

    private void setBottomNavigationMenu() {

        String prefKeyBottomNav = context.getString(R.string.pref_key_show_bottom_navigation);

        Preference.OnPreferenceChangeListener preferenceChangeListener = (preference, value) -> {
            if (getActivity() != null) {
                View bottomNavigationMenu = getActivity().findViewById(R.id.bottom_navigation);

                boolean navigationMenuEnabled = preferences.getBoolean(prefKeyBottomNav, false);
                if (navigationMenuEnabled) {
                    bottomNavigationMenu.setVisibility(View.GONE);
                } else {
                    bottomNavigationMenu.setVisibility(View.VISIBLE);
                }
            }
            return true;
        };

        Preference menuPreference = findPreference(prefKeyBottomNav);
        if (menuPreference != null) {
            menuPreference.setOnPreferenceChangeListener(preferenceChangeListener);
        }
    }

    private Runnable recreateActivity = () -> Objects.requireNonNull(getActivity()).recreate();

}
