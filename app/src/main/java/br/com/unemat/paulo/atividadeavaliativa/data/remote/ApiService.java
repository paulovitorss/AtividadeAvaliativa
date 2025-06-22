package br.com.unemat.paulo.atividadeavaliativa.data.remote;

import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/v1/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
