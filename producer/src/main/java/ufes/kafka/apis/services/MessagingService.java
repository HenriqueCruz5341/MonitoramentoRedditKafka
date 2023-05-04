package ufes.kafka.apis.services;

import retrofit2.Call;
import retrofit2.http.GET;
import ufes.kafka.apis.dtos.messaging.MessagingDto;

public interface MessagingService {
    @GET("/message/inbox")
    Call<MessagingDto> getMessaging();
}
