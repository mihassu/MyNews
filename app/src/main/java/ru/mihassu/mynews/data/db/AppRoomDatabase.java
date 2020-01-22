package ru.mihassu.mynews.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mihassu.mynews.domain.model.MyArticle;

@Database(entities = {MyArticle.class}, version = 1, exportSchema = false)

public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract MyArticleDao myArticleDao();
}