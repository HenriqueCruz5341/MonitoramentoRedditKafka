package ufes.kafka.adapters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditApi;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.services.AuthService;
import ufes.kafka.helpers.PropertiesLoader;

public class AuthAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthAdapter.class.getName());

    public Response<AuthDto> doAuth() {
        PropertiesLoader properties = new PropertiesLoader();
        Optional<String> grantTypeOpt = properties.getProperty("reddit.auth.grant_type");
        Optional<String> usernameOpt = properties.getProperty("reddit.auth.username");
        Optional<String> passwordOpt = properties.getProperty("reddit.auth.password");

        if (grantTypeOpt.isEmpty() || usernameOpt.isEmpty() || passwordOpt.isEmpty()) {
            String message = "Erro ao carregar as propriedades";
            logger.error(message);
            return Response.error(500, ResponseBody.create(null, message));
        }

        AuthService service = ClientRedditApi.getInstance().getAuthService();
        Call<AuthDto> callAuthDto = service.auth(grantTypeOpt.get(), usernameOpt.get(), passwordOpt.get());

        try {
            Response<AuthDto> responseAuthDto = callAuthDto.execute();

            if (responseAuthDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseAuthDto.code());
            } else {
                logger.info("Autenticação feita com sucesso, Token: " + responseAuthDto.body().getAccessToken());
            }

            responseAuthDto.body().setUsername(usernameOpt.get());

            return responseAuthDto;
        } catch (Exception e) {
            logger.error("Erro ao autenticar: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
