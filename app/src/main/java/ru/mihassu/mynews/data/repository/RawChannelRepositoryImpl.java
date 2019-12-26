package ru.mihassu.mynews.data.repository;

import java.util.concurrent.Callable;

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

        return Single.fromCallable(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                Response response = client
                        .newCall(new Request.Builder().url(channelUrl).build())
                        .execute();
                if(!response.isSuccessful()) {
                    throw new Exception("Channel " + channelUrl + " fetching error");
                }
                return response;
            }
        });
    }
}
