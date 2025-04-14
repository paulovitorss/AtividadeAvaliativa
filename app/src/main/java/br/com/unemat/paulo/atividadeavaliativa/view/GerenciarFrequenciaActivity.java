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
import br.com.unemat.paulo.atividadeavaliativa.controller.FrequenciaController;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;
import br.com.unemat.paulo.atividadeavaliativa.model.Frequencia;
import br.com.unemat.paulo.atividadeavaliativa.view.adapter.FrequenciaAdapter;

public class GerenciarFrequenciaActivity extends AppCompatActivity {

    private Spinner spinnerAluno, spinnerDisciplina;
    private EditText editTextTotalAulas, editTextAulasPresentes;
    private RecyclerView recyclerView;
    private FrequenciaAdapter adapter;

    private FrequenciaController frequenciaController;
    private AlunoController alunoController;

    private final List<String> disciplinas = new ArrayList<>();
    private final HashMap<String, Integer> mapaAlunos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_frequencia);

        frequenciaController = new FrequenciaController(this);
        alunoController = new AlunoController(this);

        // Iniciar componentes
        spinnerAluno = findViewById(R.id.spinnerAluno);
        spinnerDisciplina = findViewById(R.id.spinnerDisciplina);
        editTextTotalAulas = findViewById(R.id.editTextTotalAulas);
        editTextAulasPresentes = findViewById(R.id.editTextAulasPresentes);
        Button btnSalvar = findViewById(R.id.btnSalvarFrequencia);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        recyclerView = findViewById(R.id.recyclerViewFrequencia);

        setupDisciplinas();
        setupAlunos();
        setupRecyclerView();

        btnSalvar.setOnClickListener(v -> salvarFrequencia());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void setupDisciplinas() {
        disciplinas.add("Matemática");
        disciplinas.add("Português");
        disciplinas.add("História");
        disciplinas.add("Geografia");
        disciplinas.add("Ciências");
        disciplinas.add("Inglês");
        disciplinas.add("Educação Física");
        disciplinas.add("Artes");

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
        adapter = new FrequenciaAdapter(frequenciaController.getFrequencias(), this);
        recyclerView.setAdapter(adapter);
    }

    private void salvarFrequencia() {
        String alunoSelecionado = spinnerAluno.getSelectedItem().toString();
        String disciplina = spinnerDisciplina.getSelectedItem().toString();
        String totalStr = editTextTotalAulas.getText().toString().trim();
        String presentesStr = editTextAulasPresentes.getText().toString().trim();

        if (totalStr.isEmpty() || presentesStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int total = Integer.parseInt(totalStr);
            int presentes = Integer.parseInt(presentesStr);
            int alunoId = mapaAlunos.get(alunoSelecionado);

            if (presentes > total) {
                Toast.makeText(this, "Aulas presentes não pode ser maior que o total", Toast.LENGTH_SHORT).show();
                return;
            }

            Frequencia frequencia = new Frequencia(alunoId, disciplina, total, presentes);
            frequenciaController.salvar(frequencia);

            editTextTotalAulas.setText("");
            editTextAulasPresentes.setText("");
            Toast.makeText(this, "Frequência salva com sucesso", Toast.LENGTH_SHORT).show();

            adapter.atualizarLista(frequenciaController.getFrequencias());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
        }
    }
}