package ru.mihassu.mynews.data.repository;

import java.util.List;

import io.reactivex.Maybe;
import ru.mihassu.mynews.domain.model.MyArticle;

public class RoomRepoImpl implements RoomRepo {
    @Override
    public Maybe<List<MyArticle>> getArticles() {
        return null;
    }

    @Override
    public Maybe<MyArticle> getArticle() {
        return null;
    }

    @Override
    public void insertArticle(MyArticle article) {
    }

    @Override
    public void insertArticles(List<MyArticle> articles) {
    }

    @Override
    public void deleteArticle(MyArticle article) {

    }
}
