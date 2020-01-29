package ru.mihassu.mynews.data.repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import ru.mihassu.mynews.domain.channel.ChannelParser;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelRepository;
import ru.mihassu.mynews.domain.repository.RawChannelRepository;

public class ChannelRepositoryImp implements ChannelRepository {

    private RawChannelRepository repo;
    private ChannelParser parser;

    public ChannelRepositoryImp(
            RawChannelRepository repo,
            ChannelParser parser) {
        this.repo = repo;
        this.parser = parser;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<List<MyArticle>> updateChannel() {
        return repo
                .sendRequest()
                .map(this::parseToArticles
                )
                .map(this::makeFiltering)
                .toObservable();
    }

    // Распарсить XML в массив статей
    private List<MyArticle> parseToArticles(InputStream byteStream) {
        try {
            return parser.parse(byteStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Отфильтровать ненужный материал
    private List<MyArticle> makeFiltering(List<MyArticle> articlesList) {
        return articlesList
                .stream()
                // Отфильровать новости без картинок
                .filter(article -> article.image != null)
                .collect(Collectors.toList());
    }
}
