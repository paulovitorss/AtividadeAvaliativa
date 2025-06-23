package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradeRepository {
    private final ApiService apiService;

    public GradeRepository(Context context) {
        this.apiService = RetrofitClient.getClient(context).create(ApiService.class);
    }

    public LiveData<List<Grade>> getGrades(UUID studentId) {
        final MutableLiveData<List<Grade>> data = new MutableLiveData<>();

        apiService.getGrades(studentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Grade>> call, @NonNull Response<List<Grade>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Grade>> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}