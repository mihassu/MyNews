package ru.mihassu.mynews.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Single;
import ru.mihassu.mynews.data.network.ChannelParser;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ChannelRepository;
import ru.mihassu.mynews.domain.repository.RawChannelRepository;

public class ChannelRepositoryImpl implements ChannelRepository {

    private RawChannelRepository repo;

    public ChannelRepositoryImpl(RawChannelRepository repo) {
        this.repo = repo;
    }

    @Override
    public Single<List<MyArticle>> getChannel() {

        return repo
                .create()
                .map(response -> {

                    ChannelParser parser = new ChannelParser();

                    try {
                        return parser.parse(response.body().byteStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<>();
                    // Отфильровать новости без картинок
                }).map(
                        list -> {
                            return ((List<MyArticle>) list)
                                    .stream()
                                    .filter(article -> article.image != null)
                                    .collect(Collectors.toList());
                        }
                );
    }
}
