package br.com.unemat.paulo.atividadeavaliativa.model;

public class Frequencia {
    private int id;
    private int alunoId;
    private String disciplina;
    private int totalAulas;
    private int aulasPresentes;

    public Frequencia(int alunoId, String disciplina, int totalAulas, int aulasPresentes) {
        this.alunoId = alunoId;
        this.disciplina = disciplina;
        this.totalAulas = totalAulas;
        this.aulasPresentes = aulasPresentes;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public int getTotalAulas() {
        return totalAulas;
    }

    public int getAulasPresentes() {
        return aulasPresentes;
    }

    public int getPorcentagem() {
        return totalAulas == 0 ? 0 : (aulasPresentes * 100 / totalAulas);
    }
}