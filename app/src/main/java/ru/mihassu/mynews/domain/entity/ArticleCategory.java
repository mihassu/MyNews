package ru.mihassu.mynews.domain.entity;

import ru.mihassu.mynews.R;

public enum ArticleCategory {
    POLITICS(R.string.title_politics),
    ECONOMICS(R.string.title_economics),
    SOCIETY(R.string.title_society),
    SPORT(R.string.title_sport),
    CULTURE(R.string.title_culture),
    CRIME(R.string.title_crime),
    IT(R.string.title_it),
    SCIENCE(R.string.title_science),
    CELEBRITY(R.string.title_celebrity),
    TRAVEL(R.string.title_travel),
    NEWS(R.string.title_news);

    private int textId;

    ArticleCategory(int textId) {
        this.textId = textId;
    }

    public int getTextId() {
        return textId;
    }
}
