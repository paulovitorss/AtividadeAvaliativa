package br.com.unemat.paulo.atividadeavaliativa.data.remote;

import androidx.annotation.NonNull;

import java.io.IOException;

import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;

public class AuthInterceptor implements Interceptor {
    private final TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Invocation invocation = originalRequest.tag(Invocation.class);
        boolean isPublic = (invocation != null) && (invocation.method().isAnnotationPresent(Public.class));

        if (isPublic) {
            return chain.proceed(originalRequest);
        }

        Request.Builder requestBuilder = originalRequest.newBuilder();
        String token = tokenManager.getToken().blockingGet();

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(requestBuilder.build());
    }
}