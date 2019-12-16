package ru.mihassu.mynews.di;

import javax.inject.Singleton;
import dagger.Component;
import ru.mihassu.mynews.domain.repository.ArticleRepository;

@Component(modules = {
        AppComponentModule.class,
        ContextModule.class,
        DataModule.class
})

@Singleton
public interface AppComponent {

    ArticleRepository getRepository();
}
