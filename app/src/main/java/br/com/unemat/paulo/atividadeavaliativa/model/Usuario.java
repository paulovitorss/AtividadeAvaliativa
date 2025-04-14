package br.com.unemat.paulo.atividadeavaliativa.model;

public class Usuario {
    private final String nome;
    private final String cpf;
    private final String email;
    private final String telefone;
    private final String endereco;
    private final String senha;

    public Usuario(String nome, String cpf, String email, String telefone, String endereco, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getSenha() {
        return senha;
    }
}