package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService api;

    public UserRepository(Context ctx) {
        this.api = RetrofitClient.getClient(ctx).create(ApiService.class);
    }

    public LiveData<User> getUser(UUID userId) {
        MutableLiveData<User> live = new MutableLiveData<>();
        api.getUserById(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                if (resp.isSuccessful()) {
                    live.setValue(resp.body());
                } else {
                    live.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                live.setValue(null);
            }
        });
        return live;
    }
}