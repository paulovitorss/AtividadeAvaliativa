package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

public class GuardianSummary {
    @SerializedName("userId")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}