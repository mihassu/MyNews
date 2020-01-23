package ru.mihassu.mynews.di.modules.orm;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import ru.mihassu.mynews.data.db.AppRoomDatabase;
import ru.mihassu.mynews.data.db.MyArticleDao;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.data.repository.RoomRepoBookmarkImp;
import ru.mihassu.mynews.domain.model.MyArticle;

@Module
public class OrmRoomModule {

    private String dbName;

    public OrmRoomModule(String dbName) {
        this.dbName = dbName;
    }

    @Singleton
    @Provides
    public AppRoomDatabase provideRoomDatabase(Context context) {
        return Room.databaseBuilder(context, AppRoomDatabase.class, dbName).build();
    }

    @Singleton
    @Provides
    public MyArticleDao provideMyArticleDao(AppRoomDatabase db) {
        return db.myArticleDao();
    }

    @Singleton
    @Provides
    public RoomRepoBookmark provideRoomRepo(MyArticleDao myArticleDao) {
        return new RoomRepoBookmarkImp(myArticleDao);
    }

    @Singleton
    @Provides
    public Observable<List<MyArticle>> provideBookmarkObservable(RoomRepoBookmark roomRepoBookmark) {
        return roomRepoBookmark.getArticles();
    }
}
