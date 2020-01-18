package ru.mihassu.mynews.presenters;

import java.util.List;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ArticlePresenter {

    void setArticles(List<MyArticle> articles);
    List<MyArticle> getArticles();
    ArticleCategory getCategory();
}
