package ru.mihassu.mynews.ui.Fragments;

import android.os.Bundle;
import android.os.Handler;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import ru.mihassu.mynews.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private Handler handler;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        initializeThemePreference();
    }

    private void initializeThemePreference() {
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

    private Runnable recreateActivity = () -> Objects.requireNonNull(getActivity()).recreate();

}
