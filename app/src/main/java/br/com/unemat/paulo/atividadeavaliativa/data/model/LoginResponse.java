package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("userId")
    private String userId;

    @SerializedName("mainRole")
    private String mainRole;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("expiresIn")
    private Long expiresIn;

    public String getUserId() {
        return userId;
    }

    public String getMainRole() {
        return mainRole;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
