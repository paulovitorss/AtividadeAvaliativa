package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.AlunoDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;

public class DesempenhoController {
    private AlunoDAO alunoDAO;

    public DesempenhoController(Context context) {
        alunoDAO = new AlunoDAO(context);
    }

    public List<Aluno> getTodosAlunos() {
        return alunoDAO.getTodosAlunos();
    }

    public List<String> getDisciplinas() {
        return List.of("Matemática", "Português", "História", "Geografia", "Ciências", "Inglês", "Educação Física", "Artes");
    }
}