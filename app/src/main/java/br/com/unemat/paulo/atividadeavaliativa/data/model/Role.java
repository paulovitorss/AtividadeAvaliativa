package br.com.unemat.paulo.atividadeavaliativa.data.model;

import java.util.UUID;

public class Role {
    private UUID roleId;
    private String name;

    public UUID getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }
}