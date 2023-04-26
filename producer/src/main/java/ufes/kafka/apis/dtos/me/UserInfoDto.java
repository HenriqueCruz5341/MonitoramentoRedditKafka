package ufes.kafka.apis.dtos.me;

import com.google.gson.annotations.SerializedName;

public class UserInfoDto {
    @SerializedName("subscribers")
    public Integer subscribers;

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }
}
