package ru.mihassu.mynews.ui.Fragments.bookmark;

import java.util.Collections;
import java.util.List;

import ru.mihassu.mynews.domain.model.MyArticle;

public class BookmarkFragmentState {

    private List<MyArticle> articles;

    public BookmarkFragmentState(List<MyArticle> articles) {
        this.articles = articles;
    }

    public void setArticles(List<MyArticle> currentArticles) {
        this.articles = currentArticles;
    }

    public List<MyArticle> getArticles() {
        Collections.sort(this.articles);
        return articles;
    }
}
