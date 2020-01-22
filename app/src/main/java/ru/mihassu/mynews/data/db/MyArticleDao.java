package ru.mihassu.mynews.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.mihassu.mynews.domain.model.MyArticle;

@Dao
public interface MyArticleDao {

    @Query("SELECT * FROM articles")
    List<MyArticle> getAll();

    @Query("SELECT * FROM articles WHERE id = :id")
    MyArticle getById(long id);

    @Insert
    void insert(MyArticle article);

    @Update
    void update(MyArticle article);

    @Delete
    void delete(MyArticle article);
}
