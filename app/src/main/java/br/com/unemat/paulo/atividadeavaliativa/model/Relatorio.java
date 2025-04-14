package br.com.unemat.paulo.atividadeavaliativa.model;

public class Relatorio {
    private int id;
    private String tipo;
    private String filtro;
    private String dados;

    public Relatorio(String tipo, String filtro, String dados) {
        this.tipo = tipo;
        this.filtro = filtro;
        this.dados = dados;
    }

    public Relatorio(int id, String tipo, String filtro, String dados) {
        this(tipo, filtro, dados);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFiltro() {
        return filtro;
    }

    public String getDados() {
        return dados;
    }

    public void setId(int id) {
        this.id = id;
    }
}