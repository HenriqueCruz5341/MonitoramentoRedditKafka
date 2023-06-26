package ufes.kafka.dto.messaging;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DataChildrenDto {
    public String id;
    public String parentId;
    public String author;
    public String body;
    public String linkTitle;
    public long created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

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

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofInstant(new Date(created * 1000).toInstant(), ZoneId.systemDefault());
    }

    @Override
    public String toString() {
        return "DataChildrenDto{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", linkTitle='" + linkTitle + '\'' +
                ", created=" + created +
                '}';
    }
}
