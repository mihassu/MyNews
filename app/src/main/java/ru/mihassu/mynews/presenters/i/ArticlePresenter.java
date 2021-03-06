package ru.mihassu.mynews.presenters.i;

import java.util.List;

import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.viewholder.ItemUpdateListener;

public interface ArticlePresenter {

    int getTabCount();
    void onClickBookmark(long articleId);
    void onClickArticle(String articleUrl);
    void bindBookmarkChangeListener(ItemUpdateListener listener);
    List<MyArticle> getTabArticles(int tabPosition);
    List<MyArticle> getArticles();
    MyArticle getArticle(int listPosition);
    String getHighlight();
}
