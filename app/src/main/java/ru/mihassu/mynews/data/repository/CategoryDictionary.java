package ru.mihassu.mynews.data.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.mihassu.mynews.domain.entity.ArticleCategory;

import static ru.mihassu.mynews.domain.entity.ArticleCategory.*;

public class CategoryDictionary {

    private static volatile CategoryDictionary instance;

    private HashMap<ArticleCategory, List<String>> dictionary;

    public static CategoryDictionary getInstance() {
        CategoryDictionary cd = instance;

        if(cd == null) {
            synchronized (CategoryDictionary.class){
                cd = instance;
                if(cd == null) {
                    instance = cd = new CategoryDictionary();
                }
            }
        }
        return cd;
    }

    private CategoryDictionary() {
        dictionary = new HashMap<>();

        dictionary.put(POLITICS, Arrays.asList("политика", "в мире", "россия", "мир", "бывший ссср"));
        dictionary.put(ECONOMICS, Arrays.asList("экономика", "бизнес", "Недвижимость", "Оборона", "безопасность", "Нацпроекты"));
        dictionary.put(SOCIETY, Arrays.asList("общество", "дом", "из жизни", "москва", "петербург", "в россии"));
        dictionary.put(SPORT, Arrays.asList("спорт", "олимпиада"));
        dictionary.put(CULTURE, Arrays.asList("культура", "ценности"));
        dictionary.put(CRIME, Arrays.asList("силовые структуры", "криминал", "происшествия"));
        dictionary.put(IT, Arrays.asList("смартфон", "ios", "android"));
        dictionary.put(SCIENCE, Arrays.asList("наука и техника"));
        dictionary.put(CELEBRITY, Arrays.asList("стиль", "мода"));
        dictionary.put(TRAVEL, Arrays.asList("путешествия", "туризм"));
        dictionary.put(NEWS, Arrays.asList("сми", "интернет"));
    }

    public HashMap<ArticleCategory, List<String>> getDictionary() {
        return dictionary;
    }

    //Получить строковое значение категории (взять первое значение из объединенной категории)
//    public String getCategory(ArticleCategory category) {
//        if (category == NEWS) {return "Новости";}
//        return dictionary.get(category).get(0);
//    }
}
