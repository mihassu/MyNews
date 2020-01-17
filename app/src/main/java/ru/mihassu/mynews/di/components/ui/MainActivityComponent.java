package ru.mihassu.mynews.di.components.ui;

import dagger.Component;
import ru.mihassu.mynews.di.components.app.AppComponent;
import ru.mihassu.mynews.di.modules.ui.MainActivityModule;
import ru.mihassu.mynews.di.qualifiers.MainActivityScope;
import ru.mihassu.mynews.ui.MainActivity;

@MainActivityScope
@Component(dependencies = AppComponent.class, modules={MainActivityModule.class})
public interface MainActivityComponent {
    void inject(MainActivity activity);
}