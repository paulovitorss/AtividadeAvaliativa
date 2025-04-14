package br.com.unemat.paulo.atividadeavaliativa.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Frequencia;

public class FrequenciaDAO {

    private DatabaseHelper dbHelper;

    public FrequenciaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Salva ou atualiza uma frequência para um aluno em uma determinada disciplina.
    public void salvarFrequencia(Frequencia frequencia) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("aluno_id", frequencia.getAlunoId());
        values.put("disciplina", frequencia.getDisciplina());
        values.put("total_aulas", frequencia.getTotalAulas());
        values.put("aulas_presentes", frequencia.getAulasPresentes());

        // Tenta atualizar se já existir uma frequência para o mesmo aluno e disciplina.
        int updated = db.update("frequencias", values, "aluno_id=? AND disciplina=?",
                new String[]{String.valueOf(frequencia.getAlunoId()), frequencia.getDisciplina()});
        if (updated == 0) {
            // Caso não exista, insere uma nova frequência.
            db.insert("frequencias", null, values);
        }
    }

    // Retorna uma lista com todas as frequências cadastradas.
    public List<Frequencia> getTodasFrequencias() {
        List<Frequencia> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM frequencias", null);
        if (cursor.moveToFirst()) {
            do {
                Frequencia f = new Frequencia(
                        cursor.getInt(cursor.getColumnIndexOrThrow("aluno_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("disciplina")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("total_aulas")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("aulas_presentes"))
                );
                f.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                lista.add(f);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Remove a frequência de um aluno para uma determinada disciplina.
    public void removerFrequencia(int alunoId, String disciplina) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("frequencias", "aluno_id = ? AND disciplina = ?",
                new String[]{String.valueOf(alunoId), disciplina});
    }
}
