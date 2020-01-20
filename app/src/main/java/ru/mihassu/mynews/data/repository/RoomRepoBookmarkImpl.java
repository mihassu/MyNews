package ru.mihassu.mynews.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.mihassu.mynews.data.db.MyArticleDao;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.Fragments.MainFragmentState;

import static ru.mihassu.mynews.Utils.logIt;

public class RoomRepoBookmarkImpl implements RoomRepoBookmark {

    private MutableLiveData<List<MyArticle>> liveData = new MutableLiveData<>();
    private MyArticleDao myArticleDao;

    public RoomRepoBookmarkImpl(MyArticleDao myArticleDao) {
        this.myArticleDao = myArticleDao;
    }

    @Override
    public LiveData<List<MyArticle>> getArticles() {
        myArticleDao
                .getAll()
                .subscribe(
                        new DisposableObserver<List<MyArticle>>() {
                            @Override
                            public void onNext(List<MyArticle> myArticles) {
                                liveData.postValue(myArticles);
                            }
                            @Override
                            public void onError(Throwable e) {
                                logIt("ArticleDao error");
                            }
                            @Override
                            public void onComplete() {
                            }
                        }
                );

        return liveData;
    }

    @Override
    public Observable<List<MyArticle>> getArticlesRx() {
        return myArticleDao.getAll();
    }

    @Override
    public Maybe<MyArticle> getArticle(long id) {
        return myArticleDao.getById(id);
    }

    @Override
    public void insertArticle(MyArticle article) {
        executeInSeparateThread(() -> myArticleDao.insert(article));
    }

    @Override
    public void deleteArticle(MyArticle article) {
        executeInSeparateThread(() -> myArticleDao.delete(article));
    }

    @Override
    public void clearArticles() {
        myArticleDao.clear();
    }

    private void executeInSeparateThread(Runnable runnable) {
        Completable.fromRunnable(runnable)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
