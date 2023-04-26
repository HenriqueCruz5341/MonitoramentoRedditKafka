package ufes.kafka.apis.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ufes.kafka.apis.interceptors.BearerTokenInterceptor;
import ufes.kafka.apis.services.BlockedUsersService;
import ufes.kafka.apis.services.CommentService;
import ufes.kafka.apis.services.MeService;
import ufes.kafka.apis.services.SearchService;

public class ClientRedditOAuthApi {
    private static ClientRedditOAuthApi instance = null;
    public static final String BASE_URL = "https://oauth.reddit.com";

    private CommentService commentService;
    private BlockedUsersService blockedUsersService;
    private MeService meService;
    private SearchService searchService;

    public static ClientRedditOAuthApi getInstance(String accessToken) {
        if (instance == null) {
            instance = new ClientRedditOAuthApi(accessToken);
        }

        return instance;
    }

    public static ClientRedditOAuthApi refreshInstance(String accessToken) {
        instance = new ClientRedditOAuthApi(accessToken);

        return instance;
    }

    private ClientRedditOAuthApi(String accessToken) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BearerTokenInterceptor(accessToken))
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

        this.commentService = retrofit.create(CommentService.class);
        this.blockedUsersService = retrofit.create(BlockedUsersService.class);
        this.meService = retrofit.create(MeService.class);
        this.searchService = retrofit.create(SearchService.class);
    }

    public CommentService getCommentService() {
        return this.commentService;
    }

    public BlockedUsersService getBlockedUsersService() {
        return this.blockedUsersService;
    }

    public MeService getMeService() {
        return this.meService;
    }

    public SearchService getSearchService() {
        return this.searchService;
    }

}
