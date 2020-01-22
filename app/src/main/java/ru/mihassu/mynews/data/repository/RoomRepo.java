package ru.mihassu.mynews.data.repository;

import java.util.List;

import io.reactivex.Maybe;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface RoomRepo {
    Maybe<List<MyArticle>> getArticles();

    Maybe<MyArticle> getArticle();

    void insertArticle(MyArticle article);

    void insertArticles(List<MyArticle> articles);

    void deleteArticle(MyArticle article);
}
