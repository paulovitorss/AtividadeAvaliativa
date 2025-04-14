package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.dao.ComunicadoDAO;
import br.com.unemat.paulo.atividadeavaliativa.model.Comunicado;

public class ComunicadoController {
    private ComunicadoDAO comunicadoDAO;

    public ComunicadoController(Context context) {
        comunicadoDAO = new ComunicadoDAO(context);
    }

    public long salvarComunicado(Comunicado comunicado) {
        return comunicadoDAO.salvarComunicado(comunicado);
    }

    public List<Comunicado> getTodosComunicados() {
        return comunicadoDAO.getTodosComunicados();
    }

    public void removerComunicado(int id) {
        comunicadoDAO.removerComunicado(id);
    }
}