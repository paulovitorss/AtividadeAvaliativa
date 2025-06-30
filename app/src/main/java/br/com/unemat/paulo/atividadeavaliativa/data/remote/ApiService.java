package br.com.unemat.paulo.atividadeavaliativa.data.remote;

import java.util.List;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Attendance;
import br.com.unemat.paulo.atividadeavaliativa.data.model.CreateGradeRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.data.model.UpdateGradeRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @Public
    @POST("/api/v1/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("/api/v1/profile")
    Call<User> getMyProfile();

    @GET("/api/v1/grades/student/{studentId}")
    Call<List<Grade>> getGrades(@Path("studentId") UUID studentId);

    @POST("/api/v1/grades")
    Call<Grade> createGrade(@Body CreateGradeRequest request);

    @PUT("/api/v1/grades/{gradeId}")
    Call<Grade> updateGrade(@Path("gradeId") UUID gradeId, @Body UpdateGradeRequest request);

    @DELETE("/api/v1/grades/{gradeId}")
    Call<Void> deleteGrade(@Path("gradeId") UUID gradeId);

    @GET("/api/v1/attendance-records/student/{studentId}")
    Call<List<Attendance>> getAttendanceForStudent(@Path("studentId") UUID studentId);
}
