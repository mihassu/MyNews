package ru.mihassu.mynews.presenters.i;

import androidx.lifecycle.LiveData;

import ru.mihassu.mynews.ui.fragments.bookmark.BookmarkFragmentState;

public interface BookmarkFragmentPresenter extends ArticlePresenter{
    LiveData<BookmarkFragmentState> subscribe();
    void onFragmentConnected();
    void onFragmentDisconnected();
}
