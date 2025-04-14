package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.AlunoDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;

public class AlunoController {

    private AlunoDAO alunoDAO;

    public AlunoController(Context context) {
        // Inicializa o DAO com o contexto fornecido
        alunoDAO = new AlunoDAO(context);
    }

    public List<Aluno> getAlunos() {
        return alunoDAO.getTodosAlunos();
    }

    public void adicionarAluno(Aluno aluno) {
        alunoDAO.inserirAluno(aluno);
    }

    public void removerAluno(String matricula) {
        alunoDAO.removerAlunoPorMatricula(matricula);
    }

    public String getNomeAlunoPorId(String id) {
                Aluno aluno = alunoDAO.getAlunoPorId(id);
        return aluno != null ? aluno.getNome() : "Desconhecido";
    }
}