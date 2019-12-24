package ru.mihassu.mynews;

import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.model.MyArticle;

public class ArticleCategoryTest {

    @Test
    public void testEnumOrder() {

        EnumMap<ArticleCategory, List<MyArticle>> map = new EnumMap<>(ArticleCategory.class);

        for (ArticleCategory c : EnumSet.allOf(ArticleCategory.class)) {
            map.put(c, new ArrayList<>());
            System.out.println("APP_TEST: EnumSet order: " + c);
        }

        for (ArticleCategory c : map.keySet()) {
            System.out.println("APP_TEST: EnumMap order: " + c);
        }



    }
}
