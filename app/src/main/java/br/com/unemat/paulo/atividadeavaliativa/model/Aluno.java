package br.com.unemat.paulo.atividadeavaliativa.model;

public class Aluno {
    private String nome;
    private String serie;
    private String matricula;
    private String cpf;
    private String dataNascimento;
    private String senha;
    private String responsavel;
    private int id;

    public Aluno(String nome, String serie, String matricula, String cpf, String dataNascimento, String senha, String responsavel) {
        this.nome = nome;
        this.serie = serie;
        this.matricula = matricula;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
        this.responsavel = responsavel;
    }

    public String getNome() {
        return nome;
    }

    public String getSerie() {
        return serie;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getSenha() {
        return senha;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
