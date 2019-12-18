package ru.mihassu.mynews.domain.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class MyArticle implements Comparable<MyArticle> {

    public final String title;
    public final String description;
    public final String link;
    public final long pubDate;
    public final String author;
    public final String image;

    public MyArticle(String title, String description, String link, long pubDate, String author, String image) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.author = author;
        this.image = image;
    }

    @NonNull
    public String toString() {
        return new Date(pubDate).toString() + "\n" + title;
    }

    // Сортировка по дате
    @Override
    public int compareTo(@NonNull MyArticle another) {
        if (this.pubDate > another.pubDate) {
            return -1;
        } else if (this.pubDate < another.pubDate) {
            return 1;
        }
        return 0;
    }
}