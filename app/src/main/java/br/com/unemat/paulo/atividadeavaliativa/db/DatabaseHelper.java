package br.com.unemat.paulo.atividadeavaliativa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.unemat.paulo.atividadeavaliativa.model.Usuario;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tabela de Admin
    public static final String TABELA_ADMIN = "admin";
    public static final String COL_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_SENHA = "senha";
    private static final String DATABASE_NAME = "atividade_avaliativa.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Criação do banco
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela de admin
        String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABELA_ADMIN + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EMAIL + " TEXT UNIQUE, "
                + COL_SENHA + " TEXT)";
        db.execSQL(CREATE_ADMIN_TABLE);

        // Cria a tabela de usuários
        String CREATE_USUARIOS_TABLE = "CREATE TABLE usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "telefone TEXT, "
                + "endereco TEXT, "
                + "senha TEXT NOT NULL)";
        db.execSQL(CREATE_USUARIOS_TABLE);

        // Cria a tabela de responsáveis
        String CREATE_RESPONSAVEIS_TABLE = "CREATE TABLE responsaveis ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT UNIQUE NOT NULL, "
                + "email TEXT UNIQUE, "
                + "telefone TEXT, "
                + "endereco TEXT, "
                + "senha TEXT)";
        db.execSQL(CREATE_RESPONSAVEIS_TABLE);

        // Cria a tabela de alunos
        String CREATE_ALUNOS_TABLE = "CREATE TABLE IF NOT EXISTS alunos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "serie TEXT, "
                + "matricula TEXT UNIQUE, "
                + "cpf TEXT, "
                + "data_nascimento TEXT, "
                + "senha TEXT, "
                + "responsavel TEXT)";
        db.execSQL(CREATE_ALUNOS_TABLE);

        // Cria a tabela de notas
        String CREATE_NOTAS_TABLE = "CREATE TABLE IF NOT EXISTS notas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "aluno_id INTEGER NOT NULL, "
                + "disciplina TEXT NOT NULL, "
                + "nota1 REAL, "
                + "nota2 REAL, "
                + "FOREIGN KEY(aluno_id) REFERENCES alunos(id) ON DELETE CASCADE)";
        db.execSQL(CREATE_NOTAS_TABLE);

        // Cria a tabela de frequências
        String CREATE_FREQUENCIAS_TABLE = "CREATE TABLE IF NOT EXISTS frequencias ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "aluno_id INTEGER NOT NULL, "
                + "disciplina TEXT NOT NULL, "
                + "total_aulas INTEGER, "
                + "aulas_presentes INTEGER, "
                + "FOREIGN KEY(aluno_id) REFERENCES alunos(id) ON DELETE CASCADE)";
        db.execSQL(CREATE_FREQUENCIAS_TABLE);

        // Cria a tabela de comunicados
        String CREATE_COMUNICADOS_TABLE = "CREATE TABLE comunicados ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "conteudo TEXT NOT NULL, "
                + "data TEXT NOT NULL, "
                + "destinatario TEXT NOT NULL)";
        db.execSQL(CREATE_COMUNICADOS_TABLE);

        // Cria a tabela de relatórios
        String CREATE_RELATORIOS_TABLE = "CREATE TABLE IF NOT EXISTS relatorios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "tipo TEXT NOT NULL, "
                + "filtro TEXT NOT NULL, "
                + "dados TEXT NOT NULL)";
        db.execSQL(CREATE_RELATORIOS_TABLE);

        // Cria a tabela de professores
        String CREATE_PROFESSORES_TABLE = "CREATE TABLE IF NOT EXISTS professores ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT UNIQUE NOT NULL, "
                + "email TEXT UNIQUE, "
                + "telefone TEXT, "
                + "endereco TEXT, "
                + "senha TEXT)";
        db.execSQL(CREATE_PROFESSORES_TABLE);

        // Cria a tabela de disciplinas (completar conforme necessário)
        String CREATE_DISCIPLINAS_TABLE = "CREATE TABLE IF NOT EXISTS disciplinas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "professor_id INTEGER NOT NULL)";
        db.execSQL(CREATE_DISCIPLINAS_TABLE);

        // Inserir admin padrão
        ContentValues adminValues = new ContentValues();
        adminValues.put(COL_EMAIL, "admin@gov.br");
        adminValues.put(COL_SENHA, "1234");
        db.insert(TABELA_ADMIN, null, adminValues);

        // Inserir usuário de exemplo
        ContentValues userValues = new ContentValues();
        userValues.put("nome", "Joao Vitor");
        userValues.put("cpf", "12345678901");
        userValues.put("email", "joao.vitor@exemplo.com");
        userValues.put("telefone", "123456789");
        userValues.put("endereco", "Rua A, 123");
        userValues.put("senha", "1234");
        db.insert("usuarios", null, userValues);

        // Inserir responsável de exemplo
        ContentValues respValues = new ContentValues();
        respValues.put("nome", "Maria Silva");
        respValues.put("cpf", "98765432100");
        respValues.put("email", "maria.silva@exemplo.com");
        respValues.put("telefone", "987654321");
        respValues.put("endereco", "Rua B, 456");
        respValues.put("senha", "1234");
        db.insert("responsaveis", null, respValues);

        // Inserir aluno de exemplo
        ContentValues alunoValues = new ContentValues();
        alunoValues.put("nome", "Pedro Paulo");
        alunoValues.put("serie", "1º Ano");
        alunoValues.put("matricula", "PED123456");
        alunoValues.put("cpf", "11223344556");
        alunoValues.put("data_nascimento", "01/01/2010");
        alunoValues.put("senha", "1234");
        alunoValues.put("responsavel", "1");
        db.insert("alunos", null, alunoValues);

        // Inserir nota de exemplo para o aluno (aluno_id 1)
        ContentValues notaValues = new ContentValues();
        notaValues.put("aluno_id", 1);
        notaValues.put("disciplina", "Matemática");
        notaValues.put("nota1", 7.5);
        notaValues.put("nota2", 8.0);
        db.insert("notas", null, notaValues);

        // Inserir frequência de exemplo para o aluno (aluno_id 1)
        ContentValues freqValues = new ContentValues();
        freqValues.put("aluno_id", 1);
        freqValues.put("disciplina", "Português");
        freqValues.put("total_aulas", 40);
        freqValues.put("aulas_presentes", 38);
        db.insert("frequencias", null, freqValues);

        // Inserir comunicado de exemplo
        ContentValues comunValues = new ContentValues();
        comunValues.put("titulo", "Aviso Importante");
        comunValues.put("conteudo", "As aulas serão remarcadas para amanhã.");
        comunValues.put("data", "02/04/2025");
        comunValues.put("destinatario", "todos");
        db.insert("comunicados", null, comunValues);

        // Inserir relatório de exemplo
        ContentValues relValues = new ContentValues();
        relValues.put("tipo", "Desempenho");
        relValues.put("filtro", "Turma A");
        relValues.put("dados", "Dados do relatório de desempenho");
        db.insert("relatorios", null, relValues);

        // Inserir professor de exemplo
        ContentValues profValues = new ContentValues();
        profValues.put("nome", "Professor Fulano");
        profValues.put("cpf", "55566677788");
        profValues.put("email", "prof.fulano@example.com");
        profValues.put("telefone", "1122334455");
        profValues.put("endereco", "Rua C, 789");
        profValues.put("senha", "prof123");
        db.insert("professores", null, profValues);

        // Inserir disciplina de exemplo
        ContentValues discValues = new ContentValues();
        discValues.put("nome", "História");
        discValues.put("professor_id", 1);
        db.insert("disciplinas", null, discValues);
    }

    // Atualização do banco (se mudar versão)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alunos");
        db.execSQL("DROP TABLE IF EXISTS responsaveis");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS notas");
        db.execSQL("DROP TABLE IF EXISTS frequencias");
        db.execSQL("DROP TABLE IF EXISTS comunicados");
        db.execSQL("DROP TABLE IF EXISTS relatorios");
        db.execSQL("DROP TABLE IF EXISTS professores");
        db.execSQL("DROP TABLE IF EXISTS disciplinas");
        onCreate(db);
    }

    // Verificar se login do admin é válido
    public boolean validarLoginAdmin(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_ADMIN, new String[]{COL_ID}, COL_EMAIL + "=? AND " + COL_SENHA + "=?", new String[]{email, senha}, null, null, null);

        boolean valido = cursor.getCount() > 0;
        cursor.close();
        return valido;
    }

    public long inserirUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("cpf", usuario.getCpf());
        values.put("email", usuario.getEmail());
        values.put("telefone", usuario.getTelefone());
        values.put("endereco", usuario.getEndereco());
        values.put("senha", usuario.getSenha());

        return db.insert("usuarios", null, values);
    }

    // Verificar se login do usuário comum é válido
    public boolean validarLoginUsuario(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("usuarios", new String[]{"id"}, "email=? AND senha=?", new String[]{email, senha}, null, null, null);

        boolean valido = cursor.getCount() > 0;
        cursor.close();
        return valido;
    }

    public boolean validarLoginMatricula(String matricula, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("alunos",
                new String[]{"id"},
                "matricula=? AND senha=?",
                new String[]{matricula, senha},
                null, null, null);

        boolean valido = cursor.getCount() > 0;
        cursor.close();
        return valido;
    }
}