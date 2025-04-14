package br.com.unemat.paulo.atividadeavaliativa.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Comunicado;

public class ComunicadoDAO {

    private DatabaseHelper dbHelper;

    public ComunicadoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Salva (ou insere) um comunicado
    public long salvarComunicado(Comunicado comunicado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", comunicado.getTitulo());
        values.put("conteudo", comunicado.getConteudo());
        values.put("data", comunicado.getData());
        values.put("destinatario", comunicado.getDestinatario());
        return db.insert("comunicados", null, values);
    }

    // Retorna uma lista de todos os comunicados (ordenados de forma decrescente pelo id)
    public List<Comunicado> getTodosComunicados() {
        List<Comunicado> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM comunicados ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Comunicado comunicado = new Comunicado(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("conteudo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data")),
                        cursor.getString(cursor.getColumnIndexOrThrow("destinatario"))
                );
                lista.add(comunicado);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Remove um comunicado pelo ID
    public void removerComunicado(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("comunicados", "id = ?", new String[]{String.valueOf(id)});
    }
}