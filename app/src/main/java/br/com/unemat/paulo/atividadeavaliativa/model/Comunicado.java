package br.com.unemat.paulo.atividadeavaliativa.model;

public class Comunicado {
    private int id;
    private String titulo;
    private String conteudo;
    private String data;
    private String destinatario;

    public Comunicado(String titulo, String conteudo, String data, String destinatario) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.data = data;
        this.destinatario = destinatario;
    }

    public Comunicado(int id, String titulo, String conteudo, String data, String destinatario) {
        this(titulo, conteudo, data, destinatario);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getData() {
        return data;
    }

    public String getDestinatario() {
        return destinatario;
    }
}