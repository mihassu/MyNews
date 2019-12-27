package ru.mihassu.mynews.data.repository;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mihassu.mynews.domain.repository.RawChannelRepository;

public class RawChannelRepositoryImpl implements RawChannelRepository {
    private OkHttpClient client;
    private String channelUrl;

    public RawChannelRepositoryImpl(OkHttpClient client, String channelUrl) {
        this.client = client;
        this.channelUrl = channelUrl;
    }

    @Override
    public Single<Response> sendRequest() {

        return Single.fromCallable(() -> {

            Response response = null;

            try {
                response = client
                        .newCall(new Request.Builder().url(channelUrl).build())
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null && !response.isSuccessful()) {
                throw new Exception("Channel " + channelUrl + " fetching error");
            }
            return response;
        });
    }
}
