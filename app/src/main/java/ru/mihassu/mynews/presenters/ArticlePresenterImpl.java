package ru.mihassu.mynews.presenters;

import java.util.List;

import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.main.IMainAdapter;

public class ArticlePresenterImpl implements ArticlePresenter {
    private List<MyArticle> articles;
    private ArticleCategory category;
    private RoomRepoBookmark roomRepoBookmark;
    private IMainAdapter adapter;

    public ArticlePresenterImpl(ArticleCategory category, RoomRepoBookmark roomRepoBookmark) {
        this.category = category;
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
    public ArticleCategory getCategory() {
        return category;
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
        adapter.onItemUpdated(position);
    }

    @Override
    public void setAdapter(IMainAdapter adapter) {
        this.adapter = adapter;
    }
}
