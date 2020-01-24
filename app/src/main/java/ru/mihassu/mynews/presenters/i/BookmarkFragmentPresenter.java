package ru.mihassu.mynews.presenters.i;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.bookmark.BookmarkFragmentState;

public interface BookmarkFragmentPresenter extends ArticlePresenter{
    LiveData<BookmarkFragmentState> subscribe();
    void onFragmentConnected();
    void onFragmentDisconnected();
}
