package ru.mihassu.mynews.data.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.mihassu.mynews.data.db.MyArticleDao;
import ru.mihassu.mynews.domain.model.MyArticle;

public class RoomRepoBookmarkImp implements RoomRepoBookmark {

    private MyArticleDao myArticleDao;

    public RoomRepoBookmarkImp(MyArticleDao myArticleDao) {
        this.myArticleDao = myArticleDao;
    }

    @Override
    public Observable<List<MyArticle>> getBookmarkedArticles() {
        return myArticleDao.getAll();
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
