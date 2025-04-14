package br.com.unemat.paulo.atividadeavaliativa.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Responsavel;

public class ResponsavelDAO {
    private DatabaseHelper dbHelper;

    public ResponsavelDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Metodo para inserir um responsável
    public long inserirResponsavel(Responsavel responsavel) {
        // Obtém uma instância gravável do banco diretamente
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", responsavel.getNome());
        values.put("cpf", responsavel.getCpf());
        values.put("email", responsavel.getEmail());
        values.put("telefone", responsavel.getTelefone());
        values.put("endereco", responsavel.getEndereco());
        values.put("senha", responsavel.getSenha());
        return db.insert("responsaveis", null, values);
    }

    // Metodo para obter a lista de responsáveis
    public List<Responsavel> getResponsaveis() {
        List<Responsavel> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "responsaveis",
                new String[]{"id", "nome", "cpf"},
                null, null, null, null, "nome ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String cpf = cursor.getString(cursor.getColumnIndexOrThrow("cpf"));
                lista.add(new Responsavel(id, nome, cpf));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}