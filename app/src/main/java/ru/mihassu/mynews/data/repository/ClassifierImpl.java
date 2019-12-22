package ru.mihassu.mynews.data.repository;

import java.util.HashMap;
import java.util.List;

import ru.mihassu.mynews.domain.channel.Classifier;
import ru.mihassu.mynews.domain.entity.ArticleCategory;

import static ru.mihassu.mynews.domain.entity.ArticleCategory.UNKNOWN;

public class ClassifierImpl implements Classifier {

    private HashMap<ArticleCategory, List<String>> dictionary;

    public ClassifierImpl(HashMap<ArticleCategory, List<String>> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public ArticleCategory classify(String category) {
        return UNKNOWN;
    }
}
