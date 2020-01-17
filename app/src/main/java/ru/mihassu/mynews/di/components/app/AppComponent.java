package ru.mihassu.mynews.di.components.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import ru.mihassu.mynews.di.modules.app.AppModule;
import ru.mihassu.mynews.di.modules.app.NetModule;
import ru.mihassu.mynews.di.modules.orm.OrmRoomModule;

@Singleton
@Component(modules={AppModule.class, NetModule.class, OrmRoomModule.class})
public interface AppComponent {

    Application getApplicarion();
}
