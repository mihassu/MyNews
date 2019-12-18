package ru.mihassu.mynews.data.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import ru.mihassu.mynews.domain.entity.RSSNews;

public interface RegnumApi {

    @GET("rss/news/")
    Single<RSSNews> getArticles();
}
