package ru.mihassu.mynews.presenters;

import ru.mihassu.mynews.domain.entity.ArticleCategory;

public class ArticlePresenterImpl implements ArticlePresenter {
    private ArticleCategory category;

    public ArticlePresenterImpl(ArticleCategory category) {
        this.category = category;
    }
}
