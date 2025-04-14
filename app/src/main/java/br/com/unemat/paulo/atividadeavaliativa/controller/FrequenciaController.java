package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.FrequenciaDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Frequencia;

public class FrequenciaController {
    private FrequenciaDAO frequenciaDAO;

    public FrequenciaController(Context context) {
        frequenciaDAO = new FrequenciaDAO(context);
    }

    public void salvar(Frequencia freq) {
        frequenciaDAO.salvarFrequencia(freq);
    }

    public List<Frequencia> getFrequencias() {
        return frequenciaDAO.getTodasFrequencias();
    }

    public void removerFrequencia(int alunoId, String disciplina) {
        frequenciaDAO.removerFrequencia(alunoId, disciplina);
    }
}