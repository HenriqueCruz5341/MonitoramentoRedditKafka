package ufes.kafka.apis.dtos.messaging;

import com.google.gson.annotations.SerializedName;

public class ChildrenDto {
    @SerializedName("data")
    public DataChildrenDto data;

    public DataChildrenDto getData() {
        return data;
    }

    public void setData(DataChildrenDto data) {
        this.data = data;
    }
}