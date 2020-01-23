package ru.mihassu.mynews.di.modules.ui;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.di.qualifiers.BookmarkFragmentScope;
import ru.mihassu.mynews.presenters.BookmarkFragmentPresenterImp;
import ru.mihassu.mynews.presenters.i.BookmarkFragmentPresenter;
import ru.mihassu.mynews.ui.Fragments.bookmark.BookmarkAdapter;
import ru.mihassu.mynews.ui.Fragments.bookmark.BookmarksFragment;
import ru.mihassu.mynews.ui.web.BrowserLauncher;
import ru.mihassu.mynews.ui.web.BrowserLauncherImp;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

@Module
public class BookmarkFragmentModule {

    private BookmarksFragment bookmarksFragment;

    public BookmarkFragmentModule(BookmarksFragment bookmarksFragment) {
        this.bookmarksFragment = bookmarksFragment;
    }

    @Provides
    @BookmarkFragmentScope
    BrowserLauncher providesBrowserLauncher(CustomTabHelper customTabHelper) {
        return new BrowserLauncherImp(customTabHelper, bookmarksFragment.getContext());
    }

    @Provides
    @BookmarkFragmentScope
    public BookmarkFragmentPresenter provideFragmentPresenter(
            RoomRepoBookmark roomRepoBookmark,
            BrowserLauncher browserLauncher) {
        return new BookmarkFragmentPresenterImp(roomRepoBookmark, browserLauncher);
    }

    @Provides
    @BookmarkFragmentScope
    public  BookmarkAdapter provideBookmarkAdapter(BookmarkFragmentPresenter presenter) {
        return new BookmarkAdapter(presenter);
    }
}
