package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

public class StudentSummary {
    @SerializedName("userId")
    String userId;

    @SerializedName("name")
    String name;

    @SerializedName("registrationNumber")
    String registrationNumber;

    @SerializedName("series")
    String series;

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getSeries() {
        return series;
    }
}
