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
    public void setArticles(List<MyArticle> articles) {
        this.articles = articles;
    }

    @Override
    public List<MyArticle> getArticles() {
        return articles;
    }

    @Override
    public void onClickBookmark(int position) {
        MyArticle article = articles.get(position);
        article.isMarked = !article.isMarked;

        if(article.isMarked) {
            roomRepoBookmark.insertArticle(article);
        } else {
            roomRepoBookmark.deleteArticle(article);
        }
        listener.onItemUpdated(position);
    }

    @Override
    public void bindBookmarkChangeListener(BookmarkChangeListener adapter) {
        this.listener = adapter;
    }
}
