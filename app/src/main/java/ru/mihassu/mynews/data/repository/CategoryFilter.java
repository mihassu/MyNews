package ru.mihassu.mynews.data.repository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

public class CategoryFilter {

    private ArticleCategory category;
    private Observable<List<MyArticle>> articles;

    public CategoryFilter(ArticleCategory category, Observable<List<MyArticle>> articles) {
        this.category = category;
        this.articles = articles;
    }

    public Observable<List<MyArticle>> filter() {
        return articles.map(list ->
                list
                        .stream()
                        .filter(a -> a.category == category)
                        .collect(Collectors.toList())
        );
    }
}
