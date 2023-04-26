package ufes.kafka.apis.dtos.me;

import com.google.gson.annotations.SerializedName;

public class MeDto {
    @SerializedName("subreddit")
    public UserInfoDto userInfo;

    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDto userInfo) {
        this.userInfo = userInfo;
    }
}
