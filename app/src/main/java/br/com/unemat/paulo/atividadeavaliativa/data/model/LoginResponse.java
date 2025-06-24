package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("userId")
    private String userId;

    @SerializedName("role")
    private String role;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("expiresIn")
    private Long expiresIn;

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
