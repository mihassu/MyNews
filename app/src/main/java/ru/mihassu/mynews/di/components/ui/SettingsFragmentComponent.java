package ru.mihassu.mynews.di.components.ui;

import dagger.Subcomponent;
import ru.mihassu.mynews.di.modules.ui.SettingsFragmentModule;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.ui.Fragments.SettingsFragment;

@FragmentScope
@Subcomponent(modules = {SettingsFragmentModule.class})
public interface SettingsFragmentComponent {
    void inject(SettingsFragment fragment);
}
