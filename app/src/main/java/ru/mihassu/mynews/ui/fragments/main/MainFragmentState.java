package ru.mihassu.mynews.ui.fragments.main;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.DataSnapshot;
import ru.mihassu.mynews.domain.model.MyArticle;

/**
 * Хранит данные в трех структурах
 *
 * @currentArticles - общий текущий список статей
 * @currentSortedArticles - статьи отсортированы по категориям
 * @currentCategories - список категорий статей из @currentArticles
 */

public class MainFragmentState {

    private List<MyArticle> currentArticles;
    private EnumMap<ArticleCategory, List<MyArticle>> currentSortedArticles;
    private ArticleCategory[] currentCategories;
    private String highlight;

    public MainFragmentState(DataSnapshot dataSnapshot) {
        this.currentArticles = new ArrayList<>(dataSnapshot.getArticles());
        this.currentSortedArticles = sortForCategories();
        this.currentCategories = getActualCategories();
        this.highlight = dataSnapshot.getHighlight();
    }

    public MainFragmentState(List<MyArticle> currentArticles) {
        this.currentArticles = new ArrayList<>(currentArticles);
        this.currentSortedArticles = sortForCategories();
        this.currentCategories = getActualCategories();
    }

    // Раскидать статьи по категориям. При каждом обновлении создается новая
    // EnumMap со списком актуальных новостных категорий.
    private EnumMap<ArticleCategory, List<MyArticle>> sortForCategories() {

        EnumMap<ArticleCategory, List<MyArticle>> enumMap = new EnumMap<>(ArticleCategory.class);

        for (MyArticle article : currentArticles) {

            if (!enumMap.containsKey(article.category)) {
                enumMap.put(article.category, new ArrayList<>());
            }
            Objects.requireNonNull(enumMap.get(article.category)).add(article);
        }

        return enumMap;
    }

    // Получить список актуальных категорий для текущего содержимого currentArticles
    // На выходе получаем упорядоченный массив категорий.
    private ArticleCategory[] getActualCategories() {
        ArticleCategory[] actualCategories =
                new ArticleCategory[currentSortedArticles.keySet().size()];

        currentSortedArticles.keySet().toArray(actualCategories);
        return actualCategories;
    }

    public void setCurrentArticles(List<MyArticle> currentArticles) {
        this.currentArticles = new ArrayList<>(currentArticles);
        this.currentSortedArticles = sortForCategories();
        this.currentCategories = getActualCategories();
    }

    public List<MyArticle> getCurrentArticles() {
        return currentArticles;
    }

    public EnumMap<ArticleCategory, List<MyArticle>> getCurrentSortedArticles() {
        return currentSortedArticles;
    }

    public ArticleCategory[] getCurrentCategories() {
        return currentCategories;
    }

    public List<String> getCategoriesNames(Context context) {
        ArrayList<String> list = new ArrayList<>();
        Arrays.asList(currentCategories).forEach(c -> list.add(context.getString(c.getTextId())));
        return list;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }
}