package ru.mihassu.mynews.data.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.mihassu.mynews.data.network.RegnumApi;
import ru.mihassu.mynews.domain.model.MyArticle;
import ru.mihassu.mynews.domain.repository.ArticleRepository;

public class ArticleRepositoryRegnum implements ArticleRepository {

    private RegnumApi api;

    public ArticleRepositoryRegnum(RegnumApi api) {
        this.api = api;
    }

    @Override
    public Single<List<MyArticle>> getArticles() {
        return api.getArticles()
                .subscribeOn(Schedulers.io())
                .map(rssNews -> {
                    List<MyArticle> list = new ArrayList<>();
                    rssNews.getArticles().forEach(articleData ->
                            list.add(articleData.convertToModel()));
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
