package br.com.unemat.paulo.atividadeavaliativa.controller;

import android.content.Context;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Usuario;

public class UsuarioController {

    private final DatabaseHelper dbHelper;

    public UsuarioController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean validarLoginAdmin(String email, String senha) {
        return dbHelper.validarLoginAdmin(email, senha);
    }

    public long inserirUsuario(Usuario usuario) {
        return dbHelper.inserirUsuario(usuario);
    }

    public boolean validarLoginUsuario(String email, String senha) {
        return dbHelper.validarLoginUsuario(email, senha);
    }

    public boolean validarLoginMatricula(String matricula, String senha) {
        return dbHelper.validarLoginMatricula(matricula, senha);
    }
}