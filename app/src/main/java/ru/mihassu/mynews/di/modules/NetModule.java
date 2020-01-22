package ru.mihassu.mynews.di.modules;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.data.repository.ChannelCollectorImpl;
import ru.mihassu.mynews.data.repository.ChannelRepositoryImpl;
import ru.mihassu.mynews.data.repository.ClassifierImpl;
import ru.mihassu.mynews.data.repository.RawChannelRepositoryImpl;
import ru.mihassu.mynews.domain.channel.ChannelParser;
import ru.mihassu.mynews.domain.channel.Classifier;
import ru.mihassu.mynews.domain.entity.CategoryDictionary;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

@Module
public class NetModule {

    private int updateIntervalInMinutes;


    // Constructor needs one parameter to instantiate.
    public NetModule(int updateIntervalInMunutes) {
        this.updateIntervalInMinutes = updateIntervalInMunutes;
    }


    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 3 * 1024 * 1024; // 3 Mb
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Classifier provideClassifier() {
        return new ClassifierImpl(CategoryDictionary.getInstance());
    }

    @Provides
    @Singleton
    ChannelCollector provideChannelCollector(
            Context context,
            Classifier classifier,
            OkHttpClient client) {

        // URLы каналов
        String[] channelUrls = context.getResources().getStringArray(R.array.rss_channels);

        // Массив коннектов к каналам
        ArrayList<ChannelRepository> channels = new ArrayList<>();

        for (String channel : channelUrls) {
            channels.add(
                    new ChannelRepositoryImpl(
                            new RawChannelRepositoryImpl(client, channel),
                            new ChannelParser(classifier)));
        }

        return new ChannelCollectorImpl(channels, updateIntervalInMinutes);
    }
}
