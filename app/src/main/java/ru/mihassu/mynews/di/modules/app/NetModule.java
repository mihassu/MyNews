package ru.mihassu.mynews.di.modules.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import ru.mihassu.mynews.R;
import ru.mihassu.mynews.data.repository.ChannelCollectorImp;
import ru.mihassu.mynews.data.repository.ChannelRepositoryImp;
import ru.mihassu.mynews.data.repository.ClassifierImpl;
import ru.mihassu.mynews.data.repository.RawChannelRepositoryImp;
import ru.mihassu.mynews.domain.channel.ChannelParser;
import ru.mihassu.mynews.domain.channel.Classifier;
import ru.mihassu.mynews.domain.entity.CategoryDictionary;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

@Module
public class NetModule {

    private int updateIntervalInMinutes;

    // Constructor needs one parameter to instantiate.
    public NetModule(int updateIntervalInMinutes) {
        this.updateIntervalInMinutes = updateIntervalInMinutes;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Context context) {
        int cacheSize = 3 * 1024 * 1024; // 3 Mb
        return new Cache(context.getCacheDir(), cacheSize);
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

    /**
     * Вызов этого метода запустит процесс загрузки данных, т.к. ChannelCollectorImp
     * в конструктуре инициирует периодический опрос источников.
     */
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
                    new ChannelRepositoryImp(
                            new RawChannelRepositoryImp(client, channel),
                            new ChannelParser(classifier)));
        }

        return new ChannelCollectorImp(channels, updateIntervalInMinutes);
    }
}
