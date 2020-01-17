package ru.mihassu.mynews.di.components.ui;

import dagger.Component;
import ru.mihassu.mynews.di.components.app.AppComponent;
import ru.mihassu.mynews.di.modules.ui.SettingsFragmentModule;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.ui.Fragments.SettingsFragment;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = {SettingsFragmentModule.class})
public interface SettingsFragmentComponent {
    void inject(SettingsFragment fragment);

    @Component.Builder
    interface Builder {
        Builder addDependency(AppComponent appComponent);
        Builder fragmentModule(SettingsFragmentModule fragmentModule);
        SettingsFragmentComponent build();
    }
}
