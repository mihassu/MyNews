package ru.mihassu.mynews.di.modules.ui;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.presenters.ArticlePresenter;
import ru.mihassu.mynews.presenters.ArticlePresenterImpl;
import ru.mihassu.mynews.presenters.MainFragmentPresenter;
import ru.mihassu.mynews.presenters.MainFragmentPresenterImpl;

@Module
public class MainFragmentModule {

    @Provides
    @FragmentScope
    public MainFragmentPresenter provideFragmentPresenter(
            RoomRepoBookmark roomRepoBookmark,
            ChannelCollector collector) {
        return new MainFragmentPresenterImpl(roomRepoBookmark, collector);
    }

    @Provides
    @FragmentScope
    public List<ArticlePresenter> provideArticlePresentersList(RoomRepoBookmark roomRepoBookmark) {
        ArrayList<ArticlePresenter> list = new ArrayList<>();

        for(ArticleCategory category : ArticleCategory.values()) {
            list.add(new ArticlePresenterImpl(roomRepoBookmark));
        }
        return list;
    }
}
