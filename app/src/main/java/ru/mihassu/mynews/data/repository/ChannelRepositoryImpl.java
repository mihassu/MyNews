package ru.mihassu.mynews.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import ru.mihassu.mynews.domain.channel.ChannelParser;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelRepository;
import ru.mihassu.mynews.domain.repository.RawChannelRepository;

public class ChannelRepositoryImpl implements ChannelRepository {

    private RawChannelRepository repo;
    private ChannelParser parser;
    private long updateInterval;

    public ChannelRepositoryImpl(
            RawChannelRepository repo,
            ChannelParser parser,
            long updateInterval) {
        this.repo = repo;
        this.parser = parser;
        this.updateInterval = updateInterval;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<MyArticle>> getChannel() {
        return Observable
                .interval(0, updateInterval, TimeUnit.MINUTES)
                .flatMap(l ->
                        repo
                                .sendRequest()
                                .map(response -> {
                                    try {
                                        return parser.parse(response.body().byteStream());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return new ArrayList<>();
                                    // Отфильровать новости без картинок
                                })
                                .map(
                                        list -> ((List<MyArticle>) list)
                                                .stream()
                                                .filter(article -> article.image != null)
                                                .collect(Collectors.toList()))
                                .toObservable());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<MyArticle>> updateChannel() {
        return repo
                .sendRequest()
                .map(response -> {
                    try {
                        return parser.parse(response.body().byteStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<>();
                    // Отфильровать новости без картинок
                })
                .map(
                        list -> ((List<MyArticle>) list)
                                .stream()
                                .filter(article -> article.image != null)
                                .collect(Collectors.toList()))
                .toObservable();
    }
}
