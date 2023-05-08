package ufes.kafka.apis.dtos.messaging;

import com.google.gson.annotations.SerializedName;

public class DataChildrenDto {
    @SerializedName("id")
    public String id;
    @SerializedName("author")
    public String author;
    @SerializedName("body")
    public String body;
    @SerializedName("link_title")
    public String linkTitle;
    @SerializedName("created")
    public long created;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
