package ru.mihassu.mynews.data.db;

import androidx.room.TypeConverter;

import java.util.List;
import java.util.stream.Collectors;

import ru.mihassu.mynews.domain.entity.ArticleCategory;

public class CategoryConverter {

    @TypeConverter
    public static ArticleCategory toCategory(int stringId) {

        List<ArticleCategory> list =  ArticleCategory
                .toStream()
                .filter(category -> category.getTextId() == stringId)
                .collect(Collectors.toList());

        return (list.size() == 1) ? list.get(0) : ArticleCategory.NEWS;
    }

    @TypeConverter
    public static int toInt(ArticleCategory category) {
        return category.getTextId();
    }
}
