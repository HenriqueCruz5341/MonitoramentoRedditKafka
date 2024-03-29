package ufes.kafka.apis.dtos.common;

import com.google.gson.annotations.SerializedName;

public class DataPostDto {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("permalink")
    public String url;

    @SerializedName("author")
    public String author;

    @SerializedName("selftext")
    public String selftext;

    @SerializedName("created")
    public Double created;

    @SerializedName("body")
    public String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public Double getCreated() {
        return created;
    }

    public void setCreated(Double created) {
        this.created = created;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
