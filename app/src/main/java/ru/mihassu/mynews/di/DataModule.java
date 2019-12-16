package ru.mihassu.mynews.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.data.network.RegnumApi;
import ru.mihassu.mynews.data.repository.ArticleRepositoryRegnum;
import ru.mihassu.mynews.domain.repository.ArticleRepository;

@Module
public class DataModule {

    @Provides
    @Singleton
    ArticleRepository provideRepositoryRegnum(RegnumApi api) {
        return new ArticleRepositoryRegnum(api);
    }
}
