package ru.mihassu.mynews.data.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import ru.mihassu.mynews.data.entity.ArticleData;
import ru.mihassu.mynews.data.entity.RSSNews;

public interface RegnumApi {

    @GET("rss/news/")
    Single<RSSNews> getArticles();
}
