package ufes.kafka.apis.dtos.messaging;

import com.google.gson.annotations.SerializedName;

public class ChildrenDto {
    /*
     * t1 - Comment
     * t2 - User
     * t3 - Post
     * t4 - Message
     * t5 - Subreddit
     */
    @SerializedName("kind")
    public String kind;

    @SerializedName("data")
    public DataChildrenDto data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public DataChildrenDto getData() {
        return data;
    }

    public void setData(DataChildrenDto data) {
        this.data = data;
    }
}