package ru.mihassu.mynews.data.repository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface RoomRepoBookmark {
    Flowable<List<MyArticle>> getArticles();

    Maybe<MyArticle> getArticle(long id);

    void insertArticle(MyArticle article);

    void deleteArticle(MyArticle article);

    void clearArticles();
}
