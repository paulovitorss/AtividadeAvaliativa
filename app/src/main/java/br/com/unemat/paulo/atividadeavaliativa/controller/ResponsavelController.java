package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.ResponsavelDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Responsavel;

public class ResponsavelController {

    private ResponsavelDAO responsavelDAO;

    public ResponsavelController(Context context) {
        responsavelDAO = new ResponsavelDAO(context);
    }

    public long cadastrarResponsavel(Responsavel responsavel) {
        return responsavelDAO.inserirResponsavel(responsavel);
    }

    public List<Responsavel> getTodosResponsaveis() {
        return responsavelDAO.getResponsaveis();
    }
}