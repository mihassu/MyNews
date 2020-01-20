package ru.mihassu.mynews.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface RoomRepoBookmark {
    LiveData<List<MyArticle>> getArticles();
    Observable<List<MyArticle>> getArticlesRx();

    Maybe<MyArticle> getArticle(long id);

    void insertArticle(MyArticle article);

    void deleteArticle(MyArticle article);

    void clearArticles();
}
