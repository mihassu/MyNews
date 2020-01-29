package ru.mihassu.mynews.domain.model;

import java.util.List;

public class DataSnapshot {
    private List<MyArticle> articles;
    private String query;

    public DataSnapshot(List<MyArticle> articles, String query) {
        this.articles = articles;
        this.query = query;
    }

    public List<MyArticle> getArticles() {
        return articles;
    }

    public String getQuery() {
        return query;
    }
}
