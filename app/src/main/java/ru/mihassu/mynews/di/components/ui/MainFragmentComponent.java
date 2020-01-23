package ru.mihassu.mynews.di.components.ui;

import dagger.Subcomponent;
import ru.mihassu.mynews.di.modules.ui.MainFragmentModule;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.ui.Fragments.main.MainFragment;

@FragmentScope
@Subcomponent(modules = {MainFragmentModule.class})
public interface MainFragmentComponent {
    void inject(MainFragment fragment);

}
