package ru.mihassu.mynews.presenters;

import java.util.List;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.main.IMainAdapter;

public interface ArticlePresenter {

    void onClickBookmark(int position);
    void setArticles(List<MyArticle> articles);
    void setAdapter(IMainAdapter adapter);
    List<MyArticle> getArticles();
    ArticleCategory getCategory();
}
