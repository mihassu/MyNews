package ru.mihassu.mynews.domain.channel;

import ru.mihassu.mynews.domain.entity.ArticleCategory;

public interface Classifier {
    ArticleCategory classify(String category);
}
