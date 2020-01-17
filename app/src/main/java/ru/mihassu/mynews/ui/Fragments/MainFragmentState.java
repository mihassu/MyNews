package ru.mihassu.mynews.ui.Fragments;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

public class MainFragmentState {

    private List<MyArticle> currentArticles;
    private EnumMap<ArticleCategory, List<MyArticle>> currentEnumMap;
    private ArticleCategory[] currentCategories;

    public MainFragmentState(List<MyArticle> currentArticles) {
        this.currentArticles = currentArticles;
        this.currentEnumMap = sortForCategories();
        this.currentCategories = getActualCategories();
    }

    // Раскидать статьи по категориям. При каждом обновлении создается новая
    // EnumMap со списком актуальных новостных категорий.
    private EnumMap<ArticleCategory, List<MyArticle>> sortForCategories() {

        EnumMap<ArticleCategory, List<MyArticle>> enumMap = new EnumMap<>(ArticleCategory.class);

        for (MyArticle article: currentArticles) {
            if (enumMap.containsKey(article.category)) {
                Objects.requireNonNull(enumMap.get(article.category)).add(article);
            } else {
                enumMap.put(article.category, new ArrayList<>());
                Objects.requireNonNull(enumMap.get(article.category)).add(article);
            }
        }

        return enumMap;
    }

    //Получить список актуальных категорий
    private ArticleCategory[] getActualCategories() {
        ArticleCategory[] actualCategories = new ArticleCategory[currentEnumMap.keySet().size()];
        currentEnumMap.keySet().toArray(actualCategories);
        return actualCategories;
    }

    public void setCurrentArticles(List<MyArticle> currentArticles) {
        this.currentArticles = currentArticles;
        this.currentEnumMap = sortForCategories();
        this.currentCategories = getActualCategories();
    }

    public List<MyArticle> getCurrentArticles() {
        return currentArticles;
    }

    public EnumMap<ArticleCategory, List<MyArticle>> getCurrentEnumMap() {
        return currentEnumMap;
    }

    public ArticleCategory[] getCurrentCategories() {
        return currentCategories;
    }
}
