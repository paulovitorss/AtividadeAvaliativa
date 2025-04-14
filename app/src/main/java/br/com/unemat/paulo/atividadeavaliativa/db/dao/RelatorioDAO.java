package br.com.unemat.paulo.atividadeavaliativa.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Relatorio;

public class RelatorioDAO {
    private DatabaseHelper dbHelper;

    public RelatorioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insere um novo relatório no banco de dados
    public long salvarRelatorio(Relatorio relatorio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipo", relatorio.getTipo());
        values.put("filtro", relatorio.getFiltro());
        values.put("dados", relatorio.getDados());
        return db.insert("relatorios", null, values);
    }

    // Recupera todos os relatórios, ordenando-os de forma decrescente pelo ID
    public List<Relatorio> getTodosRelatorios() {
        List<Relatorio> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM relatorios ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Relatorio relatorio = new Relatorio(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("filtro")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dados"))
                );
                lista.add(relatorio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Remove um relatório com base no ID
    public void removerRelatorio(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("relatorios", "id = ?", new String[]{String.valueOf(id)});
    }
}