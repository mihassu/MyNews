package ru.mihassu.mynews.domain.repository;

import io.reactivex.Single;
import okhttp3.Response;

public interface RawChannelRepository {
    Single<Response> sendRequest();
}
