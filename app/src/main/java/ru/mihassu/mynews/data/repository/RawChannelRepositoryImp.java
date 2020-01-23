package ru.mihassu.mynews.data.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mihassu.mynews.domain.repository.RawChannelRepository;

public class RawChannelRepositoryImp implements RawChannelRepository {

    private OkHttpClient client;
    private String channelUrl;
    private byte[] errorCode;

    public RawChannelRepositoryImp(OkHttpClient client, String channelUrl) {
        this.client = client;
        this.channelUrl = channelUrl;
        errorCode = ByteBuffer
                .allocate(Integer.SIZE / Byte.SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(INVALID_RESPONSE)
                .array();
    }

    @Override
    public Single<InputStream> sendRequest() {
        InputStream error = new ByteArrayInputStream(errorCode);

        return Single.fromCallable(() -> {
            Response response;

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
