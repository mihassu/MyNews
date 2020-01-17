package ru.mihassu.mynews;

import android.app.Application;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import ru.mihassu.mynews.data.repository.ChannelCollectorImpl;
import ru.mihassu.mynews.data.repository.ChannelRepositoryImpl;
import ru.mihassu.mynews.data.repository.ClassifierImpl;
import ru.mihassu.mynews.data.repository.RawChannelRepositoryImpl;
import ru.mihassu.mynews.di.components.app.AppComponent;
import ru.mihassu.mynews.di.components.app.DaggerAppComponent;
import ru.mihassu.mynews.di.modules.app.AppModule;
import ru.mihassu.mynews.di.modules.app.NetModule;
import ru.mihassu.mynews.di.modules.orm.OrmRoomModule;
import ru.mihassu.mynews.domain.channel.ChannelParser;
import ru.mihassu.mynews.domain.channel.Classifier;
import ru.mihassu.mynews.domain.entity.CategoryDictionary;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

public class App extends Application {

    private static final int UPDATE_INTERVAL_MINUTES = 3;

    private AppComponent appComponent;

    OkHttpClient client;
    Classifier classifier;

    @Inject
    ChannelCollector collector;

    @Override
    public void onCreate() {
        super.onCreate();

//        initWithoutDagger();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(getResources().getInteger(R.integer.update_interval_minutes)))
//                .ormRoomModule(new OrmRoomModule(getString(R.string.db_name)))
                .build();

        // Создание ChannelCollector инициирует загрузку новостей
        appComponent.inject(this);
    }

    public ChannelCollector getCollector() {
        return collector;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

//    void initWithoutDagger() {
//
//        client = initOkHttpClient();
//        classifier = new ClassifierImpl(CategoryDictionary.getInstance());
//
//        // URLы каналов
//        String[] channelUrls = getResources().getStringArray(R.array.rss_channels);
//
//        // Массив коннектов к каналам
//        ArrayList<ChannelRepository> channels = new ArrayList<>();
//
//        for (String channel : channelUrls) {
//            channels.add(
//                    new ChannelRepositoryImpl(
//                            new RawChannelRepositoryImpl(client, channel),
//                            new ChannelParser(classifier)));
//        }
//
//        // В этот момент начнется загрузка данных
//        collector = new ChannelCollectorImpl(channels, getResources().getInteger(R.integer.update_interval_minutes));
//    }
//
//
//    private OkHttpClient initOkHttpClient() {
//
//        Cache cache = new Cache(this.getCacheDir(), (1024 * 1024));
//        return new OkHttpClient()
//                .newBuilder()
//                .cache(cache)
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();
//    }

}
