package br.com.unemat.paulo.atividadeavaliativa.model;

public class Nota {
    private final int alunoId;
    private final String alunoNome;
    private final String disciplina;
    private final float nota1;
    private final float nota2;
    private int id;

    public Nota(int alunoId, String alunoNome, String disciplina, float nota1, float nota2) {
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public float getNota1() {
        return nota1;
    }

    public float getNota2() {
        return nota2;
    }

    public float getMedia() {
        return (nota1 + nota2) / 2;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}