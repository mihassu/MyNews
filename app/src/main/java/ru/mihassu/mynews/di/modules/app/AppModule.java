package ru.mihassu.mynews.di.modules.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.mihassu.mynews.data.ActualDataBus;
import ru.mihassu.mynews.data.ActualDataBusImp;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.repository.ChannelCollector;

@Module
public class AppModule {

    private Context context;

    public AppModule(Application application) {
        this.context = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public BehaviorSubject providesPublisher() {
        return BehaviorSubject.create();
    }

    @Provides
    @Singleton
    public ActualDataBus providesActualDataBus(
            RoomRepoBookmark repo,
            ChannelCollector collector,
            BehaviorSubject publisher) {
        return new ActualDataBusImp(repo, collector, publisher);
    }
}
