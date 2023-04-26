package ufes.kafka.adapters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.ProducerApp;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.me.MeDto;
import ufes.kafka.apis.services.MeService;

public class MeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class.getName());

    public Optional<MeDto> getMe(String accessToken) {

        MeService service = ClientRedditOAuthApi.getInstance(accessToken).getMeService();
        Call<MeDto> callMeDto = service.getMe();

        try {
            Response<MeDto> responseMeDto = callMeDto.execute();

            if (responseMeDto.code() != 200 || !responseMeDto.isSuccessful()) {
                logger.error("Erro ao autenticar: " + responseMeDto.code());
                return Optional.empty();
            }

            MeDto meDto = responseMeDto.body();
            logger.info("Me pegado com sucesso");

            return Optional.of(meDto);
        } catch (Exception e) {
            logger.error("Erro ao autenticar: " + e.getMessage());
            return Optional.empty();
        }
    }
}
