package ufes.kafka.apis.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ufes.kafka.apis.dtos.auth.AuthDto;

public interface AuthService {
    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Call<AuthDto> auth(@Field("grant_type") String grantType, @Field("username") String username,
            @Field("password") String password);
}
