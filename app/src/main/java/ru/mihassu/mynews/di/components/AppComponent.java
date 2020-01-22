package ru.mihassu.mynews.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.mihassu.mynews.di.modules.AppModule;
import ru.mihassu.mynews.di.modules.NetModule;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface AppComponent {
//    void inject(MainActivity activity);
}
