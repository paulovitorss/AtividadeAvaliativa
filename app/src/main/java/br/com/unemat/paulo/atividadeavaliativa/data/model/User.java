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

    @SerializedName("registrationNumber")
    private String registrationNumber;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("students")
    private List<User> students;

    @SerializedName("guardians")
    private List<GuardianSummary> guardians;

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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<User> getStudents() {
        return students;
    }

    public List<GuardianSummary> getGuardians() {
        return guardians;
    }
}