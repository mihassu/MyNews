package ru.mihassu.mynews.data.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.mihassu.mynews.domain.channel.Classifier;
import ru.mihassu.mynews.domain.entity.ArticleCategory;
import ru.mihassu.mynews.domain.entity.CategoryDictionary;

import static ru.mihassu.mynews.Utils.logIt;
import static ru.mihassu.mynews.domain.entity.ArticleCategory.NEWS;

public class ClassifierImpl implements Classifier {

    private HashMap<ArticleCategory, List<String>> dictionary;

    public ClassifierImpl(CategoryDictionary categoryDictionary) {
        this.dictionary = categoryDictionary.getDictionary();
    }

    @Override
    public ArticleCategory classify(String categoryTag) {

        for (Map.Entry entry : dictionary.entrySet()) {
            for(String value : (List<String>)entry.getValue()){
                if(categoryTag.toLowerCase().contains(value.toLowerCase(Locale.getDefault()))) {
                    return (ArticleCategory)entry.getKey();
                }
            }
        }

        logIt("Unknown category found <" + categoryTag + ">. Parsed as " + NEWS);
        return NEWS;
    }
}
