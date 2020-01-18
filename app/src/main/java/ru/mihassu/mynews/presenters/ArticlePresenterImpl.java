package ru.mihassu.mynews.presenters;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.mihassu.mynews.data.repository.RoomRepo;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

public class ArticlePresenterImpl implements ArticlePresenter {
    private List<MyArticle> articles;
    private ArticleCategory category;
    private RoomRepo roomRepo;

    public ArticlePresenterImpl(ArticleCategory category, RoomRepo roomRepo) {
        this.category = category;
        this.roomRepo = roomRepo;
    }

    @Override
    public void setArticles(List<MyArticle> articles) {
        this.articles = articles;
    }

    @Override
    public List<MyArticle> getArticles() {
        return articles;
    }

    @Override
    public ArticleCategory getCategory() {
        return category;
    }
}
