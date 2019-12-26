package ru.mihassu.mynews.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;

import java.util.Objects;

import ru.mihassu.mynews.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private Handler handler;

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

        Preference themePreference = findPreference("dark_theme");
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(preferenceChangeListener);
        }
    }

    private void setBottomNavigationMenu() {
        Preference.OnPreferenceChangeListener preferenceChangeListener = (preference, value) -> {
            if (getActivity() != null) {
                View bottomNavigationMenu = getActivity().findViewById(R.id.bottom_navigation);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext()));
                boolean navigationMenuEnabled = preferences.getBoolean("show_bottom_navigation", false);
                if (navigationMenuEnabled) {
                    bottomNavigationMenu.setVisibility(View.GONE);
                } else {
                    bottomNavigationMenu.setVisibility(View.VISIBLE);
                }
            }
            return true;
        };

        Preference menuPreference = findPreference("show_bottom_navigation");
        if (menuPreference != null) {
            menuPreference.setOnPreferenceChangeListener(preferenceChangeListener);
        }
    }

    private Runnable recreateActivity = () -> Objects.requireNonNull(getActivity()).recreate();

}
