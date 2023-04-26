package ufes.kafka.apis.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ufes.kafka.apis.dtos.comment.CommentDto;

public interface CommentService {
    @GET("/comments/{id}")
    Call<List<CommentDto>> getComment(@Path("id") String id);
}
