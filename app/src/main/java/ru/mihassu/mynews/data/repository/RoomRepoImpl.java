package ru.mihassu.mynews.data.repository;

import java.util.List;

import io.reactivex.Maybe;
import ru.mihassu.mynews.data.db.MyArticleDao;
import ru.mihassu.mynews.domain.model.MyArticle;

public class RoomRepoImpl implements RoomRepo {

    private MyArticleDao myArticleDao;

    public RoomRepoImpl(MyArticleDao myArticleDao) {
        this.myArticleDao = myArticleDao;
    }

    @Override
    public Maybe<List<MyArticle>> getArticles() {
        return myArticleDao.getAll();
    }

    @Override
    public Maybe<MyArticle> getArticle(long id) {
        return myArticleDao.getById(id);
    }

    @Override
    public void insertArticle(MyArticle article) {
        myArticleDao.insert(article);
    }

    @Override
    public void deleteArticle(MyArticle article) {
        myArticleDao.delete(article);
    }

    @Override
    public void clearArticles() {
        myArticleDao.clear();
    }
}
