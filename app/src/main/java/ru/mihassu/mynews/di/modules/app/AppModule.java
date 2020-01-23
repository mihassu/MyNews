package ru.mihassu.mynews.di.modules.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;
import ru.mihassu.mynews.data.ActualDataBus;
import ru.mihassu.mynews.data.ActualDataBusImp;
import ru.mihassu.mynews.data.repository.RoomRepoBookmark;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.ui.web.CustomTabHelper;

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
    SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    @Named("data_bus_publisher")
    BehaviorSubject<List<MyArticle>> providesPublisher() {
        return BehaviorSubject.create();
    }

    @Provides
    @Singleton
    ActualDataBus providesActualDataBus(
            RoomRepoBookmark repo,
            ChannelCollector collector,
            @Named("data_bus_publisher") BehaviorSubject<List<MyArticle>> publisher) {
        return new ActualDataBusImp(repo, collector, publisher);
    }

    @Provides
    @Singleton
    CustomTabHelper providesCustomTabHelper() {
        return new CustomTabHelper();
    }
}
