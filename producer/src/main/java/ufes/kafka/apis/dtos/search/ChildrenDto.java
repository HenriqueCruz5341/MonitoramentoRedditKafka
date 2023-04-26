package ufes.kafka.apis.dtos.search;

import com.google.gson.annotations.SerializedName;

public class ChildrenDto {
    @SerializedName("data")
    public DataPostDto dataPost;

    public DataPostDto getDataPost() {
        return dataPost;
    }

    public void setDataPost(DataPostDto dataPost) {
        this.dataPost = dataPost;
    }
}