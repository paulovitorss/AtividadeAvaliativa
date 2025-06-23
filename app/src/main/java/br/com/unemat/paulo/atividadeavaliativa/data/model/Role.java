package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("role_id")
    private Long roleId;

    @SerializedName("name")
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }
}