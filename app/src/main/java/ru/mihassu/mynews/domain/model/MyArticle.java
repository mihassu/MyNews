package ru.mihassu.mynews.domain.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

import ru.mihassu.mynews.data.db.CategoryConverter;
import ru.mihassu.mynews.domain.entity.ArticleCategory;

@Entity(tableName = "articles")
public class MyArticle implements Comparable<MyArticle> {

    @PrimaryKey(autoGenerate = false)
    public long id;

    @ColumnInfo(name = "pub_date")
    public long pubDate;

    @TypeConverters(CategoryConverter.class)
    public ArticleCategory category;

    @ColumnInfo(name = "category_origin")
    public String categoryOrigin;

    public String title;
    public String description;
    public String link;
    public String author;
    public String image;

    @ColumnInfo(name = "marked")
    public boolean isMarked;

    public MyArticle() {
    }

    @Ignore
    public MyArticle(String title,
                     String description,
                     String link,
                     long pubDate,
                     String author,
                     String image,
                     boolean isMarked,
                     String categoryOrigin,
                     ArticleCategory category) {
        this.id = calculateId(title, pubDate);
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.author = author;
        this.image = image;
        this.categoryOrigin = categoryOrigin;
        this.category = category;
        this.isMarked = isMarked;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isMarked, pubDate);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final MyArticle other = (MyArticle)obj;
        return this.id == other.id &&
                this.isMarked == other.isMarked &&
                this.pubDate == other.pubDate;
    }

    // Вычислить Id
    private long calculateId(String title, long date) {
        return Math.abs(title.hashCode()) + date;
    }
}