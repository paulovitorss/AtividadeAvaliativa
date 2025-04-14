package br.com.unemat.paulo.atividadeavaliativa.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.db.DatabaseHelper;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;

public class AlunoDAO {
    private DatabaseHelper dbHelper;

    public AlunoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Obtém um aluno pelo ID (passado como String)
    public Aluno getAlunoPorId(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "alunos", // Nome da tabela
                new String[]{"id", "nome", "serie", "matricula", "cpf", "data_nascimento", "senha", "responsavel"}, // Colunas a serem selecionadas
                "id = ?",
                new String[]{id},
                null, null, null);

        Aluno aluno = null;
        if (cursor.moveToFirst()) {
            aluno = new Aluno(
                    cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    cursor.getString(cursor.getColumnIndexOrThrow("serie")),
                    cursor.getString(cursor.getColumnIndexOrThrow("matricula")),
                    cursor.getString(cursor.getColumnIndexOrThrow("cpf")),
                    cursor.getString(cursor.getColumnIndexOrThrow("data_nascimento")),
                    cursor.getString(cursor.getColumnIndexOrThrow("senha")),
                    cursor.getString(cursor.getColumnIndexOrThrow("responsavel"))
            );
            aluno.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        }
        cursor.close();
        return aluno;
    }

    // Insere um aluno na tabela
    public void inserirAluno(Aluno aluno) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("serie", aluno.getSerie());
        values.put("matricula", aluno.getMatricula());
        values.put("cpf", aluno.getCpf());
        values.put("data_nascimento", aluno.getDataNascimento());
        values.put("senha", aluno.getSenha());
        values.put("responsavel", aluno.getResponsavel());
        db.insert("alunos", null, values);
    }

    // Obtém todos os alunos da tabela
    public List<Aluno> getTodosAlunos() {
        List<Aluno> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("alunos",
                new String[]{"id", "nome", "serie", "matricula", "cpf", "data_nascimento", "senha", "responsavel"},
                null, null, null, null, "nome ASC");

        if (cursor.moveToFirst()) {
            do {
                Aluno aluno = new Aluno(
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("serie")),
                        cursor.getString(cursor.getColumnIndexOrThrow("matricula")),
                        cursor.getString(cursor.getColumnIndexOrThrow("cpf")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data_nascimento")),
                        cursor.getString(cursor.getColumnIndexOrThrow("senha")),
                        cursor.getString(cursor.getColumnIndexOrThrow("responsavel"))
                );
                aluno.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                lista.add(aluno);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Remove um aluno com base na matrícula
    public void removerAlunoPorMatricula(String matricula) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("alunos", "matricula = ?", new String[]{matricula});
    }
}