package ru.mihassu.mynews.di.components.ui;

import dagger.Component;
import ru.mihassu.mynews.di.components.app.AppComponent;
import ru.mihassu.mynews.di.modules.ui.ViewPagerModule;
import ru.mihassu.mynews.di.qualifiers.ViewPagerScope;
import ru.mihassu.mynews.ui.news.NewsViewPagerAdapter;

@ViewPagerScope
@Component(dependencies = AppComponent.class, modules = {ViewPagerModule.class})
public interface ViewPagerComponent {
    void inject(NewsViewPagerAdapter adapter);

    @Component.Builder
    interface Builder {
        Builder addDependency(AppComponent appComponent);
        Builder fragmentModule(ViewPagerModule module);
        ViewPagerComponent build();
    }
}
