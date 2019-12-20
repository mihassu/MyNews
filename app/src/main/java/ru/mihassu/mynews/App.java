package ru.mihassu.mynews;

import android.app.Application;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import ru.mihassu.mynews.data.repository.RawChannelRepositoryImpl;
import ru.mihassu.mynews.data.repository.ChannelCollectorImpl;
import ru.mihassu.mynews.data.repository.ChannelRepositoryImpl;
import ru.mihassu.mynews.domain.repository.ChannelCollector;
import ru.mihassu.mynews.domain.repository.ChannelRepository;

public class App extends Application {

    OkHttpClient client;
    ChannelCollector collector;

    @Override
    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient();

        // URLы каналов
        String[] channelUrls = getResources().getStringArray(R.array.rss_channels);

        // Массив коннектов к каналам
        ArrayList<ChannelRepository> channels = new ArrayList<>();

        for(String channel : channelUrls) {
            channels.add(new ChannelRepositoryImpl(new RawChannelRepositoryImpl(client, channel)));
        }

        collector = new ChannelCollectorImpl(channels);
    }

    public ChannelCollector getCollector() {
        return collector;

    }
}
