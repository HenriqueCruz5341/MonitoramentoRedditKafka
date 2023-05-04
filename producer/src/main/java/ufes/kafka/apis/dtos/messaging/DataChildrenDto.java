package ufes.kafka.apis.dtos.messaging;

import com.google.gson.annotations.SerializedName;

public class DataChildrenDto {
    @SerializedName("author")
    public String author;
    @SerializedName("body")
    public String body;
    @SerializedName("link_title")
    public String linkIitle;


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

    public String getLinkIitle() {
        return linkIitle;
    }

    public void setLinkIitle(String linkIitle) {
        this.linkIitle = linkIitle;
    }
    
}
