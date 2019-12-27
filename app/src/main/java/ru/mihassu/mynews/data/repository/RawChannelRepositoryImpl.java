package ru.mihassu.mynews.data.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mihassu.mynews.domain.repository.RawChannelRepository;

public class RawChannelRepositoryImpl implements RawChannelRepository {

    private static final int RETRY_COUNT = 3;

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
        }).retry(RETRY_COUNT);
    }

    @Override
    public Single<InputStream> sendRequestEx() {

        InputStream error = new ByteArrayInputStream(new byte[]{0});

        return Single.fromCallable(() -> {

            Response response = null;

            try {
                response = client
                        .newCall(new Request.Builder().url(channelUrl).build())
                        .execute();
            } catch (IOException e) {
                return error;
            }

            if (!response.isSuccessful()) {
                return error;
            }

            return response.body().byteStream();
        }).retry(RETRY_COUNT);
    }

}
