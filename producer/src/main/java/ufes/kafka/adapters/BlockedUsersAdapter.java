package ufes.kafka.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.blocked.BlockedUsersDto;
import ufes.kafka.apis.services.BlockedUsersService;

public class BlockedUsersAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BlockedUsersAdapter.class.getName());

    public Response<BlockedUsersDto> getBlockedUsers(String accessToken) {

        BlockedUsersService service = ClientRedditOAuthApi.getInstance(accessToken).getBlockedUsersService();
        Call<BlockedUsersDto> callBlockedUsersDto = service.getBlockedUsers();

        try {
            Response<BlockedUsersDto> responseBlockedUsersDto = callBlockedUsersDto.execute();

            if (responseBlockedUsersDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseBlockedUsersDto.code());
            } else {
                logger.info("BlockedUsers pego com sucesso");
            }

            return responseBlockedUsersDto;
        } catch (Exception e) {
            logger.error("Erro ao pegar blocked users: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
