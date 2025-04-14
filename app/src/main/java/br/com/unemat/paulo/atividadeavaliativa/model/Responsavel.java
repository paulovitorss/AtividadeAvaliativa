package br.com.unemat.paulo.atividadeavaliativa.model;

public class Responsavel {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    private String senha;
    private int id;

    public Responsavel(String nome, String cpf, String email, String telefone, String endereco, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.senha = senha;
    }

    public Responsavel(int id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
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

    public int getId() {
        return id;
    }
}