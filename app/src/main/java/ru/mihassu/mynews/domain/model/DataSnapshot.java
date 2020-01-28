package ru.mihassu.mynews.domain.model;

import java.util.List;

public class DataSnapshot {
    private List<MyArticle> articles;
    private String highlight;

    public DataSnapshot(List<MyArticle> articles, String highlight) {
        this.articles = articles;
        this.highlight = highlight;
    }

    public List<MyArticle> getArticles() {
        return articles;
    }

    public String getHighlight() {
        return highlight;
    }
}
