package ru.mihassu.mynews.domain.entity;

public enum ArticleCategory {
    POLITICS("Политика"),
    ECONOMICS("Экономика"),
    SOCIETY("Общество"),
    SPORT("Спорт"),
    CULTURE("Культура"),
    CRIME("Криминал"),
    IT("Интернет"),
    SCIENCE("Наука"),
    CELEBRITY("Стиль"),
    TRAVEL("Путешествия"),
    NEWS("Новости");

    private String description;

    ArticleCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
