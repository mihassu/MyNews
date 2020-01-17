package ru.mihassu.mynews.di.modules.orm;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.mynews.data.db.AppRoomDatabase;
import ru.mihassu.mynews.data.db.MyArticleDao;
import ru.mihassu.mynews.data.repository.RoomRepo;
import ru.mihassu.mynews.data.repository.RoomRepoImpl;

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
    public RoomRepo provideRoomRepo(MyArticleDao myArticleDao) {
        return new RoomRepoImpl(myArticleDao);
    }
}
