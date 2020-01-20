package ru.mihassu.mynews.di.modules.ui;

import java.util.HashMap;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.presenters.ArticlePresenter;
import ru.mihassu.mynews.presenters.ArticlePresenterImpl;
import ru.mihassu.mynews.presenters.MainFragmentPresenter;
import ru.mihassu.mynews.presenters.MainFragmentPresenterImpl;

@Module
public class MainFragmentModule {

    @Provides
    @FragmentScope
    public MainFragmentPresenter provideFragmentPresenter(RoomRepoBookmark roomRepoBookmark) {
        return new MainFragmentPresenterImpl(roomRepoBookmark);
    }

    @Provides
    @FragmentScope
    public HashMap<ArticleCategory, ArticlePresenter> provideArticlePresenters(RoomRepoBookmark roomRepoBookmark) {
        HashMap<ArticleCategory, ArticlePresenter> map = new HashMap<>();
        for(ArticleCategory category : ArticleCategory.values()) {
            map.put(category, new ArticlePresenterImpl(category, roomRepoBookmark));
        }
        return map;
    }
}
