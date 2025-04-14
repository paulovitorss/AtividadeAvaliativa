package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.RelatorioDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Relatorio;

public class RelatorioController {
    private RelatorioDAO relatorioDAO;

    public RelatorioController(Context context) {
        relatorioDAO = new RelatorioDAO(context);
    }

    public long salvarRelatorio(Relatorio relatorio) {
        return relatorioDAO.salvarRelatorio(relatorio);
    }

    public List<Relatorio> getRelatorios() {
        return relatorioDAO.getTodosRelatorios();
    }

    public void removerRelatorio(int id) {
        relatorioDAO.removerRelatorio(id);
    }
}