package ufes.kafka.apis.services;

import retrofit2.Call;
import retrofit2.http.GET;
import ufes.kafka.apis.dtos.blocked.BlockedUsersDto;

public interface BlockedUsersService {
    @GET("/prefs/blocked")
    Call<BlockedUsersDto> getBlockedUsers();
}
