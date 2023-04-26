package ufes.kafka.apis.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BearerTokenInterceptor implements Interceptor {

    private String accessToken;

    public BearerTokenInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws java.io.IOException {
        Request request = chain.request();
        request = request.newBuilder().header("Authorization", "Bearer " + accessToken).build();
        return chain.proceed(request);
    }
}
