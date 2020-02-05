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
 * @currentCategories - список категорий статей из @lastUpdateArticles
 */

public class MainFragmentState {

    private List<MyArticle> lastUpdateArticles;
    private EnumMap<ArticleCategory, List<MyArticle>> currentSortedArticles;
    private ArticleCategory[] currentCategories;
    private String query;

    public MainFragmentState(DataSnapshot dataSnapshot) {
        this.lastUpdateArticles = new ArrayList<>(dataSnapshot.getArticles());
        this.currentSortedArticles = sortForCategories(this.lastUpdateArticles);
        this.currentCategories = getActualCategories();
        this.query = dataSnapshot.getQuery();
    }

    // Раскидать статьи по категориям. При каждом обновлении создается новая
    // EnumMap со списком актуальных новостных категорий.
    private EnumMap<ArticleCategory, List<MyArticle>> sortForCategories(List<MyArticle> currentArticles) {

        EnumMap<ArticleCategory, List<MyArticle>> enumMap = new EnumMap<>(ArticleCategory.class);

        for (MyArticle article : currentArticles) {

            if (!enumMap.containsKey(article.category)) {
                enumMap.put(article.category, new ArrayList<>());
            }
            Objects.requireNonNull(enumMap.get(article.category)).add(article);
        }

        return enumMap;
    }

    // Получить список актуальных категорий для текущего содержимого lastUpdateArticles
    // На выходе получаем упорядоченный массив категорий.
    private ArticleCategory[] getActualCategories() {
        ArticleCategory[] actualCategories =
                new ArticleCategory[currentSortedArticles.keySet().size()];

        currentSortedArticles.keySet().toArray(actualCategories);
        return actualCategories;
    }

    public void updateState(DataSnapshot dataSnapshot) {
        String query = dataSnapshot.getQuery();
        List<MyArticle> currentArticles = new ArrayList<>(dataSnapshot.getArticles());

        if (query.isEmpty()) {
            this.lastUpdateArticles = currentArticles;
        }

        this.currentSortedArticles = sortForCategories(currentArticles);
        this.currentCategories = getActualCategories();
        this.query = query;
    }

    public List<MyArticle> getLastUpdateArticles() {
        return lastUpdateArticles;
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

    public String getQuery() {
        return query;
    }

    public boolean isUpdateRequired() {

        // Для теста на 10 минут
        final long hourSeconds = 60 * 10;
        long moreRecent = 0;

        for (MyArticle article : lastUpdateArticles) {
            if (article.pubDate > moreRecent) {
                moreRecent = article.pubDate;
            }
        }

        return (System.currentTimeMillis() - moreRecent) / 1000 > hourSeconds;
    }
}
