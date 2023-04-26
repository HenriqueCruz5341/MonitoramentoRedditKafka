package ufes.kafka.adapters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditApi;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.services.AuthService;
import ufes.kafka.helpers.PropertiesLoader;

public class AuthAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthAdapter.class.getName());

    public Optional<AuthDto> doAuth() {
        PropertiesLoader properties = new PropertiesLoader();
        Optional<String> grantTypeOpt = properties.getProperty("reddit.auth.grant_type");
        Optional<String> usernameOpt = properties.getProperty("reddit.auth.username");
        Optional<String> passwordOpt = properties.getProperty("reddit.auth.password");

        if (grantTypeOpt.isEmpty() || usernameOpt.isEmpty() || passwordOpt.isEmpty()) {
            logger.error("Erro ao carregar as propriedades");
            return Optional.empty();
        }

        AuthService service = ClientRedditApi.getInstance().getAuthService();
        Call<AuthDto> callAuthDto = service.auth(grantTypeOpt.get(), usernameOpt.get(), passwordOpt.get());

        try {
            Response<AuthDto> responseAuthDto = callAuthDto.execute();

            if (responseAuthDto.code() != 200 || !responseAuthDto.isSuccessful()) {
                logger.error("Erro ao autenticar: " + responseAuthDto.code());
                return Optional.empty();
            }

            AuthDto authDto = responseAuthDto.body();
            logger.info("Autenticação feita com sucesso, Token: " + authDto.getAccessToken());

            return Optional.of(authDto);
        } catch (Exception e) {
            logger.error("Erro ao autenticar: " + e.getMessage());
            return Optional.empty();
        }
    }
}
