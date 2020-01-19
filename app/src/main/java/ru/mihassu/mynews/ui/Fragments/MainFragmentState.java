package ru.mihassu.mynews.ui.Fragments;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

public class MainFragmentState {

    public static final String EMPTY_SEARCH = "";

    private List<MyArticle> currentArticles;
    private EnumMap<ArticleCategory, List<MyArticle>> currentEnumMap;
    private ArticleCategory[] currentCategories;

    public MainFragmentState(List<MyArticle> currentArticles) {
        this.currentArticles = currentArticles;
        this.currentEnumMap = sortForCategories();
        this.currentCategories = getCategories();
    }

    // Раскидать статьи по категориям
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

    //Получить список категорий
    private ArticleCategory[] getCategories() {
        Set<ArticleCategory> currents = currentEnumMap.keySet();
        return currents.toArray(new ArticleCategory[currents.size()]);
    }

    public void setCurrentArticles(List<MyArticle> currentArticles) {
        this.currentArticles = currentArticles;
        this.currentEnumMap = sortForCategories();
        this.currentCategories = getCategories();
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
