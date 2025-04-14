package br.com.unemat.paulo.atividadeavaliativa.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.AlunoController;
import br.com.unemat.paulo.atividadeavaliativa.controller.ResponsavelController;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;
import br.com.unemat.paulo.atividadeavaliativa.model.Responsavel;

public class AdicionarAlunoActivity extends AppCompatActivity {

    private final Calendar calendar = Calendar.getInstance();
    private final HashMap<String, String> mapResponsaveis = new HashMap<>();
    private EditText editTextNomeAluno, editTextSerieAluno, editTextCpfAluno, editTextDataNascimentoAluno, editTextMatriculaAluno, editTextSenhaAluno;
    private AutoCompleteTextView autoCompleteResponsavel;
    private AlunoController alunoController;
    private ResponsavelController responsavelController;
    private String responsavelSelecionadoId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_aluno);

        // Inicializa componentes
        editTextNomeAluno = findViewById(R.id.editTextNomeAluno);
        editTextSerieAluno = findViewById(R.id.editTextSerieAluno);
        editTextCpfAluno = findViewById(R.id.editTextCpfAluno);
        editTextDataNascimentoAluno = findViewById(R.id.editTextDataNascimentoAluno);
        editTextMatriculaAluno = findViewById(R.id.editTextMatriculaAluno);
        editTextSenhaAluno = findViewById(R.id.editTextSenhaAluno);
        autoCompleteResponsavel = findViewById(R.id.autoCompleteResponsavel);

        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnSalvar = findViewById(R.id.btnSalvarAluno);
        Button btnNovoResponsavel = findViewById(R.id.btnNovoResponsavel);

        alunoController = new AlunoController(getApplicationContext());
        responsavelController = new ResponsavelController(getApplicationContext());

        // Gera matrícula automaticamente
        String matriculaGerada = gerarMatricula(editTextNomeAluno.getText().toString());
        editTextMatriculaAluno.setText(matriculaGerada);

        // Carrega responsáveis no AutoComplete
        carregarResponsaveis();

        // Date picker
        editTextDataNascimentoAluno.setOnClickListener(v -> mostrarDatePicker());

        btnCancelar.setOnClickListener(v -> finish());
        btnSalvar.setOnClickListener(v -> salvarAluno());

        // Ação do botão "Cadastrar novo responsável"
        btnNovoResponsavel.setOnClickListener(v -> {
            Intent intent = new Intent(AdicionarAlunoActivity.this, AdicionarResponsavelActivity.class);
            startActivity(intent);
        });
    }

    private void mostrarDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            editTextDataNascimentoAluno.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void carregarResponsaveis() {
        List<Responsavel> lista = responsavelController.getTodosResponsaveis();

        if (lista == null || lista.isEmpty()) return;

        String[] opcoes = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Responsavel r = lista.get(i);
            String exibir = r.getNome() + " - " + r.getCpf();
            opcoes[i] = exibir;
            mapResponsaveis.put(exibir, String.valueOf(r.getId()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcoes);

        autoCompleteResponsavel.setAdapter(adapter);

        autoCompleteResponsavel.setOnItemClickListener((parent, view, position, id) -> {
            String itemSelecionado = (String) parent.getItemAtPosition(position);
            responsavelSelecionadoId = mapResponsaveis.get(itemSelecionado);
        });
    }

    private void salvarAluno() {
        String nome = editTextNomeAluno.getText().toString().trim();
        String serie = editTextSerieAluno.getText().toString().trim();
        String cpf = editTextCpfAluno.getText().toString().trim();
        String dataNascimento = editTextDataNascimentoAluno.getText().toString().trim();
        String matricula = gerarMatricula(nome);
        editTextMatriculaAluno.setText(matricula);
        String senha = editTextSenhaAluno.getText().toString().trim();

        if (nome.isEmpty() || serie.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty() || matricula.isEmpty() || senha.isEmpty() || responsavelSelecionadoId == null) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Aluno aluno = new Aluno(nome, serie, matricula, cpf, dataNascimento, senha, responsavelSelecionadoId);
        alunoController.adicionarAluno(aluno);

        Toast.makeText(this, "Aluno salvo com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarResponsaveis(); // Recarrega a lista toda vez que a tela voltar a aparecer
    }

    private String gerarMatricula(String nome) {
        String prefixo = nome.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (prefixo.length() > 3) prefixo = prefixo.substring(0, 3);
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(7);
        return prefixo + timestamp;
    }
}