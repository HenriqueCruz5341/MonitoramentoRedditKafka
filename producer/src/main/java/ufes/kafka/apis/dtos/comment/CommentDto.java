package ufes.kafka.apis.dtos.comment;

import com.google.gson.annotations.SerializedName;

public class CommentDto {
    @SerializedName("data")
    public DataDto data;

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
    }
}
