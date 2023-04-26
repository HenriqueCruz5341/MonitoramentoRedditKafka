package ufes.kafka.apis.services;

import retrofit2.Call;
import retrofit2.http.GET;
import ufes.kafka.apis.dtos.me.MeDto;

public interface MeService {
    @GET("/api/v1/me")
    Call<MeDto> getMe();
}
