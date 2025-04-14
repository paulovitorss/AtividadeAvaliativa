package br.com.unemat.paulo.atividadeavaliativa.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Nota;

public class NotaDAO {
    private DatabaseHelper dbHelper;

    public NotaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Salva ou atualiza uma nota.
    public void salvarNota(Nota nota) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("aluno_id", nota.getAlunoId());
        values.put("disciplina", nota.getDisciplina());
        values.put("nota1", nota.getNota1());
        values.put("nota2", nota.getNota2());

        // Verifica se já existe uma nota para o mesmo aluno e disciplina
        Cursor cursor = db.query(
                "notas",
                null,
                "aluno_id=? AND disciplina=?",
                new String[]{String.valueOf(nota.getAlunoId()), nota.getDisciplina()},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            // Atualiza a nota existente
            db.update("notas", values, "aluno_id=? AND disciplina=?",
                    new String[]{String.valueOf(nota.getAlunoId()), nota.getDisciplina()});
        } else {
            // Insere uma nova nota
            db.insert("notas", null, values);
        }
        cursor.close();
    }

    public List<Nota> getTodasNotas() {
        List<Nota> lista = new ArrayList<>();
        String query = "SELECT notas.aluno_id, alunos.nome, notas.disciplina, notas.nota1, notas.nota2 " +
                "FROM notas INNER JOIN alunos ON alunos.id = notas.aluno_id " +
                "ORDER BY alunos.nome ASC";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int alunoId = cursor.getInt(cursor.getColumnIndexOrThrow("aluno_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String disciplina = cursor.getString(cursor.getColumnIndexOrThrow("disciplina"));
                float nota1 = cursor.getFloat(cursor.getColumnIndexOrThrow("nota1"));
                float nota2 = cursor.getFloat(cursor.getColumnIndexOrThrow("nota2"));
                Nota nota = new Nota(alunoId, nome, disciplina, nota1, nota2);
                lista.add(nota);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Remove uma nota baseada no alunoId e na disciplina.
    public void removerNota(int alunoId, String disciplina) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("notas", "aluno_id=? AND disciplina=?",
                new String[]{String.valueOf(alunoId), disciplina});
    }
}