package ufes.kafka.apis.dtos.messaging;

import com.google.gson.annotations.SerializedName;

public class MessagingDto {
    @SerializedName("data")
    public DataDto data;

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
    }
}
