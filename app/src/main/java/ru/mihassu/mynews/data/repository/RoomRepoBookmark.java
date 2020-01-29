package ru.mihassu.mynews.data.repository;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface RoomRepoBookmark {
    Observable<List<MyArticle>> getBookmarkedArticles();

    void insertArticle(MyArticle article);

    void deleteArticle(MyArticle article);

    void clearArticles();
}
