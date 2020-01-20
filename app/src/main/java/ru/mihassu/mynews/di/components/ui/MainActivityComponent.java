package ru.mihassu.mynews.di.components.ui;

import dagger.Subcomponent;
import ru.mihassu.mynews.di.modules.ui.MainActivityModule;
import ru.mihassu.mynews.di.qualifiers.MainActivityScope;
import ru.mihassu.mynews.ui.MainActivity;

@MainActivityScope
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
