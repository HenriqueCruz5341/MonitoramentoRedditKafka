package ufes.kafka.apis.clients;

import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ufes.kafka.apis.interceptors.BasicAuthInterceptor;
import ufes.kafka.apis.services.AuthService;
import ufes.kafka.helpers.PropertiesLoader;

public class ClientRedditApi {
    private static ClientRedditApi instance = null;
    public static final String BASE_URL = "https://www.reddit.com";

    private AuthService authService;

    public static ClientRedditApi getInstance() {
        if (instance == null) {
            instance = new ClientRedditApi();
        }

        return instance;
    }

    private ClientRedditApi() {
        PropertiesLoader properties = new PropertiesLoader();
        Optional<String> clientId = properties.getProperty("reddit.api.client.id");
        Optional<String> clientSecret = properties.getProperty("reddit.api.client.secret");

        if (clientId.isEmpty() || clientSecret.isEmpty()) {
            throw new RuntimeException("Erro ao carregar as propriedades");
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(clientId.get(), clientSecret.get()))
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        buildRetrofit(client, gson, BASE_URL);

    }

    private void buildRetrofit(OkHttpClient client, Gson gson, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.authService = retrofit.create(AuthService.class);
    }

    public AuthService getAuthService() {
        return this.authService;
    }

}
