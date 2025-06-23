package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.CreateGradeRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.model.UpdateGradeRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.RetrofitClient;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradeRepository {
    private final ApiService apiService;
    private final TokenManager tokenManager;

    public GradeRepository(Context context) {
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
        this.tokenManager = TokenManager.getInstance(context);
    }

    public LiveData<List<Grade>> getGrades(UUID studentId) {
        MutableLiveData<List<Grade>> data = new MutableLiveData<>();

        String token = tokenManager.getTokenSync();
        String bearer = "Bearer " + token;

        apiService.getGrades(studentId, bearer)
                .enqueue(new Callback<>() {
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

    public LiveData<Grade> createGrade(CreateGradeRequest request) {
        MutableLiveData<Grade> data = new MutableLiveData<>();

        apiService.createGrade(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Grade> call, @NonNull Response<Grade> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Grade> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<Grade> updateGrade(UUID gradeId, UpdateGradeRequest request) {
        MutableLiveData<Grade> data = new MutableLiveData<>();

        apiService.updateGrade(gradeId, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Grade> call, @NonNull Response<Grade> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Grade> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<Boolean> deleteGrade(UUID gradeId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        apiService.deleteGrade(gradeId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }
}