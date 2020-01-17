package ru.mihassu.mynews.di.modules.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.di.qualifiers.MainActivityScope;

@Module
public class AppModule {

    private Application app;

    public AppModule(Application application) {
        this.app = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
