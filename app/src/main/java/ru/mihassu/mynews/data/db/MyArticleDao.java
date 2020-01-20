package ru.mihassu.mynews.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import ru.mihassu.mynews.domain.model.MyArticle;

@Dao
public interface MyArticleDao {

    /**
     * Записи есть - onSuccess
     * Записей нет - onComplete и пустой список
     */
    @Query("SELECT * FROM articles")
    Flowable<List<MyArticle>> getAll();

    /**
     * Запись есть - onSuccess
     * Записи нет - onComplete
     */
    @Query("SELECT * FROM articles WHERE id = :id")
    Maybe<MyArticle> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MyArticle article);

    @Delete
    void delete(MyArticle article);

    @Query("delete from articles")
    void clear();
}
