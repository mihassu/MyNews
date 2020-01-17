package ru.mihassu.mynews;

import android.app.Application;

import javax.inject.Inject;

import ru.mihassu.mynews.di.components.app.AppComponent;
import ru.mihassu.mynews.di.components.app.DaggerAppComponent;
import ru.mihassu.mynews.di.modules.app.AppModule;
import ru.mihassu.mynews.di.modules.app.NetModule;
import ru.mihassu.mynews.di.modules.orm.OrmRoomModule;
import ru.mihassu.mynews.domain.repository.ChannelCollector;

public class App extends Application {

    private AppComponent appComponent;

    @Inject
    ChannelCollector collector;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(getResources().getInteger(R.integer.update_interval_minutes)))
                .ormRoomModule(new OrmRoomModule(getString(R.string.db_name)))
                .build();

        // Создание ChannelCollector инициирует загрузку новостей
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
