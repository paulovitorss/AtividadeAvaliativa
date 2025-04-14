package br.com.unemat.paulo.atividadeavaliativa.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.AlunoController;
import br.com.unemat.paulo.atividadeavaliativa.controller.NotaController;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;
import br.com.unemat.paulo.atividadeavaliativa.model.Nota;
import br.com.unemat.paulo.atividadeavaliativa.view.adapter.GerenciarNotasAdapter;

public class GerenciarNotasActivity extends AppCompatActivity {

    private final List<String> disciplinas = new ArrayList<>();
    private final HashMap<String, Integer> mapaAlunos = new HashMap<>();
    private Spinner spinnerAluno, spinnerDisciplina;
    private EditText editTextNota1, editTextNota2;
    private RecyclerView recyclerView;
    private NotaController notaController;
    private AlunoController alunoController;
    private GerenciarNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_notas);

        // Iniciar controllers
        notaController = new NotaController(this);
        alunoController = new AlunoController(this);

        // Inicializar componentes
        spinnerAluno = findViewById(R.id.spinnerAluno);
        spinnerDisciplina = findViewById(R.id.spinnerDisciplina);
        editTextNota1 = findViewById(R.id.editTextNota1);
        editTextNota2 = findViewById(R.id.editTextNota2);
        Button btnSalvar = findViewById(R.id.btnSalvarNotas);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        recyclerView = findViewById(R.id.recyclerViewNotas);

        setupDisciplinas();
        setupAlunos();
        setupRecyclerView();

        btnSalvar.setOnClickListener(v -> salvarNota());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void setupDisciplinas() {
        disciplinas.add("Matemática");
        disciplinas.add("Português");
        disciplinas.add("História");
        disciplinas.add("Geografia");
        disciplinas.add("Ciências");
        disciplinas.add("Inglês");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, disciplinas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisciplina.setAdapter(adapter);
    }

    private void setupAlunos() {
        List<Aluno> alunos = alunoController.getAlunos();
        List<String> nomes = new ArrayList<>();

        for (Aluno aluno : alunos) {
            String display = aluno.getNome() + " - " + aluno.getSerie();
            nomes.add(display);
            mapaAlunos.put(display, aluno.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nomes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAluno.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GerenciarNotasAdapter(notaController.getNotas(), this);
        recyclerView.setAdapter(adapter);
    }

    private void salvarNota() {
        String alunoSelecionado = spinnerAluno.getSelectedItem().toString();
        String disciplina = spinnerDisciplina.getSelectedItem().toString();
        String nota1Str = editTextNota1.getText().toString().trim();
        String nota2Str = editTextNota2.getText().toString().trim();

        if (nota1Str.isEmpty() || nota2Str.isEmpty()) {
            Toast.makeText(this, "Preencha todas as notas", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float nota1 = Float.parseFloat(nota1Str);
            float nota2 = Float.parseFloat(nota2Str);

            int alunoId = mapaAlunos.get(alunoSelecionado);

            Nota nota = new Nota(alunoId, alunoSelecionado.split(" - ")[0], disciplina, nota1, nota2);
            notaController.salvarNota(nota);
            adapter.atualizarLista(notaController.getNotas());

            editTextNota1.setText("");
            editTextNota2.setText("");
            Toast.makeText(this, "Nota salva com sucesso", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Notas devem ser números válidos", Toast.LENGTH_SHORT).show();
        }
    }
}