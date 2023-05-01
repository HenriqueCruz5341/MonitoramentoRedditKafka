package ufes.kafka.apis.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ufes.kafka.apis.dtos.overview.OverviewDto;

public interface OverviewService {
    @GET("/user/{username}/overview")
    Call<OverviewDto> getOverview(@Path("username") String username);

}
