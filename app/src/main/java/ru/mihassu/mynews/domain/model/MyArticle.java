package ru.mihassu.mynews.domain.model;

public class MyArticle {

    private String title;
    private String content;
    private String link;

    public MyArticle(String title, String link, String content) {
        this.title = title;
        this.link = link;
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

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
}
