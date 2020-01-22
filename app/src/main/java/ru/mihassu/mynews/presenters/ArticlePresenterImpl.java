package ru.mihassu.mynews.presenters;

import java.util.List;

import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.main.BookmarkChangeListener;

public class ArticlePresenterImpl implements ArticlePresenter {
    private List<MyArticle> articles;
    private RoomRepoBookmark roomRepoBookmark;
    private BookmarkChangeListener listener;

    public ArticlePresenterImpl(RoomRepoBookmark roomRepoBookmark) {
        this.roomRepoBookmark = roomRepoBookmark;
    }

    @Override
    public int getTabCount() {
        return 0;
    }

    @Override
    public List<MyArticle> getArticles() {
        return null;
    }

    @Override
    public List<MyArticle> getTabArticles(int tabPosition) {
        return articles;
    }

    @Override
    public void onClickBookmark(long articleId) {
//        MyArticle article = articles.get(articleId);
//        article.isMarked = !article.isMarked;
//
//        if(article.isMarked) {
//            roomRepoBookmark.insertArticle(article);
//        } else {
//            roomRepoBookmark.deleteArticle(article);
//        }
//        listener.onItemUpdated(articleId);
    }




    @Override
    public void bindBookmarkChangeListener(BookmarkChangeListener adapter) {
        this.listener = adapter;
    }

    @Override
    public List<MyArticle> getArticle(int position) {
        return null;
    }

    @Override
    public void onClickArticle(long articleId) {

    }

}
