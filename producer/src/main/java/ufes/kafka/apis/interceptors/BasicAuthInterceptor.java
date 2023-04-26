package ufes.kafka.apis.interceptors;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor(String username, String password) {
        this.credentials = Credentials.basic(username, password);
    }

    @Override
    public Response intercept(Chain chain) throws java.io.IOException {
        Request request = chain.request();
        request = request.newBuilder().header("Authorization", credentials).build();
        return chain.proceed(request);
    }

}
