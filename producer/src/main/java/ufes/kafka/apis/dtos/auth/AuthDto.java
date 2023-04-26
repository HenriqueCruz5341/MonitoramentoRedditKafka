package ufes.kafka.apis.dtos.auth;

import com.google.gson.annotations.SerializedName;

public class AuthDto {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("bearer")
    private String bearer;

    @SerializedName("expires_in")
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

}