package ru.mihassu.mynews.di.modules.app;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }
}
