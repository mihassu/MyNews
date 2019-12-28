package ru.mihassu.mynews.domain.repository;

import java.util.List;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.model.MyArticle;

public interface ChannelRepository {
    Observable<List<MyArticle>> updateChannel();
}
