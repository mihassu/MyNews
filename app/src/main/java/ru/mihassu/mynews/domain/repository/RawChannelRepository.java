package ru.mihassu.mynews.domain.repository;

import java.io.InputStream;

import io.reactivex.Single;
import okhttp3.Response;

public interface RawChannelRepository {
    int INVALID_RESPONSE = -1;
    Single<Response> sendRequest();
    Single<InputStream> sendRequestEx();
}
