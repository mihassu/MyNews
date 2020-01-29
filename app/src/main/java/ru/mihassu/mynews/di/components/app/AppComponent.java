package ru.mihassu.mynews.di.components.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.mihassu.mynews.App;
import ru.mihassu.mynews.di.components.ui.BookmarkFragmentComponent;
import ru.mihassu.mynews.di.components.ui.MainActivityComponent;
import ru.mihassu.mynews.di.components.ui.MainFragmentComponent;
import ru.mihassu.mynews.di.components.ui.SettingsFragmentComponent;
import ru.mihassu.mynews.di.modules.app.AppModule;
import ru.mihassu.mynews.di.modules.app.NetModule;
import ru.mihassu.mynews.di.modules.orm.OrmRoomModule;
import ru.mihassu.mynews.di.modules.ui.BookmarkFragmentModule;
import ru.mihassu.mynews.di.modules.ui.MainActivityModule;
import ru.mihassu.mynews.di.modules.ui.MainFragmentModule;
import ru.mihassu.mynews.di.modules.ui.SettingsFragmentModule;

@Singleton
@Component(modules={AppModule.class, NetModule.class, OrmRoomModule.class})
public interface AppComponent {

    void inject(App application);

    MainActivityComponent plusMainActivityComponent(MainActivityModule module);
    MainFragmentComponent plusMainFragmentComponent(MainFragmentModule module);
    BookmarkFragmentComponent plusBookmarkFragmentComponent(BookmarkFragmentModule module);
    SettingsFragmentComponent plusSettingsFragmentComponent(SettingsFragmentModule module);

}
