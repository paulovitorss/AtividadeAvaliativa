package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;

    public AuthRepository() {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<LoginResponse> login(LoginRequest loginRequest) {
        final MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        apiService.login(loginRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}