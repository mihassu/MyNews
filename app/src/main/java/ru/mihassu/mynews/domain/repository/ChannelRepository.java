package ru.mihassu.mynews.domain.repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ChannelRepository {
    Single<List<MyArticle>> getChannel();
    Observable<List<MyArticle>> getChannelObs();
}
