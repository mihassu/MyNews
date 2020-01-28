package ru.mihassu.mynews.di.modules.ui;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.data.eventbus.ActualDataBus;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.di.qualifiers.FragmentScope;
import ru.mihassu.mynews.domain.model.DataSnapshot;
import ru.mihassu.mynews.presenters.i.ArticlePresenter;
import ru.mihassu.mynews.presenters.i.MainFragmentPresenter;
import ru.mihassu.mynews.presenters.MainFragmentPresenterImp;
import ru.mihassu.mynews.presenters.RegularArticlePresenter;
import ru.mihassu.mynews.ui.web.BrowserLauncher;
import ru.mihassu.mynews.ui.web.BrowserLauncherImp;
import ru.mihassu.mynews.ui.fragments.main.MainFragment;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

@Module
public class MainFragmentModule {

    private MainFragment mainFragment;

    public MainFragmentModule(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    @Provides
    @FragmentScope
    public MainFragmentPresenter provideFragmentPresenter(ActualDataBus dataBus) {
        return new MainFragmentPresenterImp(dataBus);
    }

    @Provides
    @FragmentScope
    @Named("search_result_publisher")
    public BehaviorSubject<DataSnapshot> provideBehaviorSubject() {
        return BehaviorSubject.create();
    }

    @Provides
    @FragmentScope
    public ArticlePresenter provideArticlePresenter(ActualDataBus dataBus,
                                                    RoomRepoBookmark repoBookmark,
                                                    BrowserLauncher browserLauncher) {
        return new RegularArticlePresenter(dataBus, repoBookmark, browserLauncher);
    }

    @Provides
    @FragmentScope
    BrowserLauncher providesBrowserLauncher(CustomTabHelper customTabHelper) {
        return new BrowserLauncherImp(customTabHelper, mainFragment.getContext());
    }
}
