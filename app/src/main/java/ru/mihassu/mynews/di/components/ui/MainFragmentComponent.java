package ru.mihassu.mynews.di.components.ui;

import dagger.Component;
import ru.mihassu.mynews.di.components.app.AppComponent;
import ru.mihassu.mynews.di.modules.ui.MainFragmentModule;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.ui.Fragments.MainFragment;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = {MainFragmentModule.class})
public interface MainFragmentComponent {
    void inject(MainFragment fragment);

    @Component.Builder
    interface Builder {

//        @BindsInstance
//        Builder bindFragment(MainFragment fragment);

        Builder addDependency(AppComponent appComponent);

        Builder fragmentModule(MainFragmentModule fragmentModule);

        MainFragmentComponent build();
    }
}
