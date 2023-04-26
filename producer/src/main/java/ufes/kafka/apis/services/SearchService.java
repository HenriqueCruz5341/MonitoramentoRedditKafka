package ufes.kafka.apis.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ufes.kafka.apis.dtos.search.SearchDto;

public interface SearchService {
    @GET("/search")
    Call<SearchDto> search(@Query("q") String query);
}
