package ru.mihassu.mynews.presenters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.observers.DisposableObserver;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.presenters.i.BookmarkFragmentPresenter;
import ru.mihassu.mynews.ui.Fragments.bookmark.BookmarkFragmentState;
import ru.mihassu.mynews.ui.main.ItemUpdateListener;
import ru.mihassu.mynews.ui.web.BrowserLauncher;

import static ru.mihassu.mynews.Utils.logIt;

public class BookmarkFragmentPresenterImp implements BookmarkFragmentPresenter {

    private MutableLiveData<BookmarkFragmentState> liveData = new MutableLiveData<>();
    private RoomRepoBookmark roomRepoBookmark;
    private BrowserLauncher browserLauncher;

    public BookmarkFragmentPresenterImp(RoomRepoBookmark roomRepoBookmark,
                                        BrowserLauncher browserLauncher) {
        this.roomRepoBookmark = roomRepoBookmark;
        this.browserLauncher = browserLauncher;
        connectToRepo();
    }

    @Override
    public LiveData<BookmarkFragmentState> subscribe() {
        return liveData;
    }

    private void connectToRepo() {
        roomRepoBookmark
                .getArticles()
                .subscribe(new DisposableObserver<List<MyArticle>>() {
                    @Override
                    public void onNext(List<MyArticle> list) {
                        logIt("BookmarkPresenter received " + list.size() + " articles");
                        if (liveData.getValue() != null) {
                            BookmarkFragmentState currentState = liveData.getValue();
                            currentState.setArticles(list);
                            liveData.postValue(currentState);
                        } else {
                            liveData.postValue(new BookmarkFragmentState(list));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        logIt("BookmarkFragmentPresenter: error in subscribeToDataSources method");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public int getTabCount() {
        return 0;
    }

    @Override
    public void onClickBookmark(long articleId) {
        MyArticle article = findArticle(articleId);

        if (article != null) {
            article.isMarked = !article.isMarked;

            logIt("Bookmark clicked on '" + article.title + "'");

            // Обновить базу
            if (article.isMarked) {
                roomRepoBookmark.insertArticle(article);
            } else {
                roomRepoBookmark.deleteArticle(article);
            }
        }
    }

    @Override
    public void onClickArticle(String articleUrl) {
        browserLauncher.showInBrowser(articleUrl);
    }

    @Override
    public List<MyArticle> getTabArticles(int tabPosition) {
        return getArticles();
    }

    @Override
    public List<MyArticle> getArticles() {
        if (liveData.getValue() != null) {
            BookmarkFragmentState currentState = liveData.getValue();
            return currentState.getArticles();
        }
        return new ArrayList<>();
    }

    @Override
    public MyArticle getArticle(int listPosition) {
        if (liveData.getValue() != null) {
            BookmarkFragmentState currentState = liveData.getValue();
            return currentState.getArticles().get(listPosition);
        }
        return null;
    }

    @Override
    public void bindBookmarkChangeListener(ItemUpdateListener listener) {
    }

    // Найти статью в общем списке по её ID
    private MyArticle findArticle(long articleId) {
        if (liveData.getValue() != null) {
            BookmarkFragmentState currentState = liveData.getValue();
            return currentState
                    .getArticles()
                    .stream()
                    .filter((a) -> a.id == articleId)
                    .collect(Collectors.toList())
                    .get(0);
        }

        return null;
    }
}
