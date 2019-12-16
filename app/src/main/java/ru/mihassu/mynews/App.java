package ru.mihassu.mynews;

import android.app.Application;

import ru.mihassu.mynews.di.AppComponent;
import ru.mihassu.mynews.di.AppComponentModule;
import ru.mihassu.mynews.di.DaggerAppComponent;
import ru.mihassu.mynews.di.DataModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appComponentModule(new AppComponentModule())
                .dataModule(new DataModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
