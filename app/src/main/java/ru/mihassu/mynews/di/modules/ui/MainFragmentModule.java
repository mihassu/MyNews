package ru.mihassu.mynews.di.modules.ui;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.data.ActualDataBus;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.ArticlePresenter;
import ru.mihassu.mynews.presenters.MainFragmentPresenter;
import ru.mihassu.mynews.presenters.MainFragmentPresenterImp;
import ru.mihassu.mynews.presenters.RegularArticlePresenter;

@Module
public class MainFragmentModule {

    @Provides
    @FragmentScope
    public MainFragmentPresenter provideFragmentPresenter(ActualDataBus dataBus) {
        return new MainFragmentPresenterImp(dataBus);
    }

    @Provides
    @FragmentScope
    public BehaviorSubject<List<MyArticle>> provideBehaviorSubject() {
        return BehaviorSubject.create();
    }

    @Provides
    @FragmentScope
    public ArticlePresenter provideArticlePresenter(ActualDataBus dataBus,
                                                    RoomRepoBookmark repoBookmark) {

        return new RegularArticlePresenter(dataBus, repoBookmark);

    }

//    @Provides
//    @FragmentScope
//    public List<ArticlePresenter> provideArticlePresentersList(RoomRepoBookmark roomRepoBookmark) {
//        ArrayList<ArticlePresenter> list = new ArrayList<>();
//
//        for(ArticleCategory category : ArticleCategory.values()) {
//            list.add(new ArticlePresenterImpl(roomRepoBookmark));
//        }
//        return list;
//    }
}
