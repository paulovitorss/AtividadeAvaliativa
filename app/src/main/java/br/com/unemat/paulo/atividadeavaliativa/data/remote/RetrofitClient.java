package br.com.unemat.paulo.atividadeavaliativa.data.remote;

import android.content.Context;

import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.70.200.122:8080/";
    private static volatile Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {

            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    TokenManager tokenManager = TokenManager.getInstance(context);
                    AuthInterceptor authInterceptor = new AuthInterceptor(tokenManager);

                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(authInterceptor)
                            .addInterceptor(loggingInterceptor)
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}