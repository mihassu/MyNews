package ru.mihassu.mynews.presenters;

import java.util.List;

import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.main.BookmarkChangeListener;

public interface ArticlePresenter {

    void onClickBookmark(int position);
    void setArticles(List<MyArticle> articles);
    void bindBookmarkChangeListener(BookmarkChangeListener listener);
    List<MyArticle> getArticles();
}
