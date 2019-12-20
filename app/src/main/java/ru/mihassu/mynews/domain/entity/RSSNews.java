package ru.mihassu.mynews.domain.entity;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root (name = "rss", strict = false)
public class RSSNews {

    @ElementList (name = "item", inline = true)
    @Path("channel")
    private List<ArticleData> articles;

    public List<ArticleData> getArticles() {
        return articles;
    }
}
