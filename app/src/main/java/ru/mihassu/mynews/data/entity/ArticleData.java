package ru.mihassu.mynews.data.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ru.mihassu.mynews.domain.model.MyArticle;

@Root(name = "item", strict = false)
public class ArticleData {

    @Element (name = "title")
    private String title;

    @Element (name = "description")
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyArticle convertToModel() {
        return new MyArticle(title, content);
    }
}
