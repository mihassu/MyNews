package ru.mihassu.mynews;

import android.app.Application;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import ru.mihassu.mynews.data.repository.CategoryDictionary;
import ru.mihassu.mynews.data.repository.ClassifierImpl;
import ru.mihassu.mynews.data.repository.RawChannelRepositoryImpl;
import ru.mihassu.mynews.data.repository.ChannelCollectorImpl;
import ru.mihassu.mynews.data.repository.ChannelRepositoryImpl;
import ru.mihassu.mynews.domain.channel.ChannelParser;
import ru.mihassu.mynews.domain.channel.Classifier;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

public class App extends Application {

    OkHttpClient client;
    ChannelCollector collector;
    Classifier classifier;

    @Override
    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient();
        classifier = new ClassifierImpl(CategoryDictionary.getInstance());

        // URLы каналов
        String[] channelUrls = getResources().getStringArray(R.array.rss_channels);

        // Массив коннектов к каналам
        ArrayList<ChannelRepository> channels = new ArrayList<>();

        for(String channel : channelUrls) {
            channels.add(
                    new ChannelRepositoryImpl(
                            new RawChannelRepositoryImpl(client, channel),
                            new ChannelParser(classifier)));
        }

        collector = new ChannelCollectorImpl(channels);
    }

    public ChannelCollector getCollector() {
        return collector;

    }
}
