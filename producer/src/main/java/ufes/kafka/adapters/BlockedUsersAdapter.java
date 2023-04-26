package ufes.kafka.adapters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.ProducerApp;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.blocked.BlockedUsersDto;
import ufes.kafka.apis.services.BlockedUsersService;

public class BlockedUsersAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class.getName());

    public Optional<BlockedUsersDto> getBlockedUsers(String accessToken) {

        BlockedUsersService service = ClientRedditOAuthApi.getInstance(accessToken).getBlockedUsersService();
        Call<BlockedUsersDto> callBlockedUsersDto = service.getBlockedUsers();

        try {
            Response<BlockedUsersDto> responseBlockedUsersDto = callBlockedUsersDto.execute();

            if (responseBlockedUsersDto.code() != 200 || !responseBlockedUsersDto.isSuccessful()) {
                logger.error("Erro ao autenticar: " + responseBlockedUsersDto.code());
                return Optional.empty();
            }

            BlockedUsersDto blockedUsersDto = responseBlockedUsersDto.body();
            logger.info("BlockedUsers pegado com sucesso");

            return Optional.of(blockedUsersDto);
        } catch (Exception e) {
            logger.error("Erro ao autenticar: " + e.getMessage());
            return Optional.empty();
        }
    }
}
