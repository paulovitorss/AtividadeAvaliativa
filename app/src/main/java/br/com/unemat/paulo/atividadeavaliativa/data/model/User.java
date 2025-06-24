package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class User {
    @SerializedName("userId")
    private UUID userId;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("email")
    private String email;

    @SerializedName("roles")
    private List<Role> roles;

    @SerializedName("series")
    private String series;

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }


    public String getCpf() {
        return cpf;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getSeries() {
        return series;
    }
}