package ru.mihassu.mynews.presenters;

import java.util.List;

import ru.mihassu.mynews.data.repository.RoomRepo;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.ui.main.IMainAdapter;

public class ArticlePresenterImpl implements ArticlePresenter {
    private List<MyArticle> articles;
    private ArticleCategory category;
    private RoomRepo roomRepo;
    private IMainAdapter adapter;

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


    @Override
    public void onClickBookmark(int position) {
        MyArticle article = articles.get(position);
        article.isMarked = !article.isMarked;

        if(article.isMarked) {
            roomRepo.insertArticle(article);
        } else {
            roomRepo.deleteArticle(article);
        }
        adapter.onItemUpdated(position);
    }

    @Override
    public void setAdapter(IMainAdapter adapter) {
        this.adapter = adapter;
    }
}
