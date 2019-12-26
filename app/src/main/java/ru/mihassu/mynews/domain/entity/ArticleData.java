package ru.mihassu.mynews.domain.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ru.mihassu.mynews.domain.model.MyArticle;

@Root(name = "item", strict = false)
public class ArticleData {


    @Element(name = "link")
    public String link;

    @Element(name = "title")
    public String title;

    @Element(name = "description")
    public String description;

    @Element(name = "pubDate")
    public long pubDate;

    @Element(name = "author")
    public String author;

    @Element(name = "image")
    public String image;

    @Element(name = "category")
    public String categoryOrigin;

    public MyArticle convertToModel() {
        return new MyArticle(
                title, description, link, pubDate, author, image, categoryOrigin, ArticleCategory.NEWS
        );
    }
}