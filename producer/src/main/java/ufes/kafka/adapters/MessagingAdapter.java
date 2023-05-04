package ufes.kafka.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.messaging.MessagingDto;
import ufes.kafka.apis.services.MessagingService;

public class MessagingAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MessagingAdapter.class.getName());

    public Response<MessagingDto> getMessaging(String accessToken) {

        MessagingService service = ClientRedditOAuthApi.getInstance(accessToken).getMessagingService();
        Call<MessagingDto> callMessagingDto = service.getMessaging();

        try {
            Response<MessagingDto> responseMessagingDto = callMessagingDto.execute();

            if (responseMessagingDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseMessagingDto.code());
            } else {
                logger.info("Mensagens pegas com sucesso");
            }

            return responseMessagingDto;
        } catch (Exception e) {
            logger.error("Erro as mensagens: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
