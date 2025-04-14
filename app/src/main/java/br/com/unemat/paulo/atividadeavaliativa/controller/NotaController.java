package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.NotaDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Nota;

public class NotaController {
    private NotaDAO notaDAO;

    public NotaController(Context context) {
        notaDAO = new NotaDAO(context);
    }

    public void salvarNota(Nota nota) {
        notaDAO.salvarNota(nota);
    }

    public List<Nota> getNotas() {
        return notaDAO.getTodasNotas();
    }

    public void removerNota(int alunoId, String disciplina) {
        notaDAO.removerNota(alunoId, disciplina);
    }
}