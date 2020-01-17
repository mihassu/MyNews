package ru.mihassu.mynews.di.modules.ui;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.di.qualifiers.MainActivityScope;

@Module
public class MainActivityModule {

    @Provides
    @MainActivityScope
    public SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
