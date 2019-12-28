package ru.mihassu.mynews.domain.repository;

import java.io.InputStream;

import io.reactivex.Single;

public interface RawChannelRepository {
    int INVALID_RESPONSE = -1;
    int RETRY_COUNT = 3;
    Single<InputStream> sendRequest();
}
