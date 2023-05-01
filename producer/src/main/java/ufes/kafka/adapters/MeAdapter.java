package ufes.kafka.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.me.MeDto;
import ufes.kafka.apis.services.MeService;

public class MeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MeAdapter.class.getName());

    public Response<MeDto> getMe(String accessToken) {

        MeService service = ClientRedditOAuthApi.getInstance(accessToken).getMeService();
        Call<MeDto> callMeDto = service.getMe();

        try {
            Response<MeDto> responseMeDto = callMeDto.execute();

            if (responseMeDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseMeDto.code());
            } else {
                logger.info("Me pego com sucesso");
            }

            return responseMeDto;
        } catch (Exception e) {
            logger.error("Erro ao autenticar: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
