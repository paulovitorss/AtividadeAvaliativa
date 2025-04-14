package br.com.unemat.paulo.atividadeavaliativa.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.AlunoController;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;
import br.com.unemat.paulo.atividadeavaliativa.view.adapter.AlunosAdapter;

public class GerenciarAlunosActivity extends AppCompatActivity {

    private AlunosAdapter adapter;
    private List<Aluno> listaCompleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_alunos);

        AlunoController controller = new AlunoController(this);
        listaCompleta = controller.getAlunos();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAlunos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlunosAdapter(listaCompleta, controller);
        recyclerView.setAdapter(adapter);

        TextInputEditText editSearch = findViewById(R.id.editTextSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.btnAdicionarAluno).setOnClickListener(v ->
                startActivity(new Intent(this, AdicionarAlunoActivity.class))
        );

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    private void filtrar(String texto) {
        List<Aluno> filtrada = new ArrayList<>();
        for (Aluno a : listaCompleta) {
            if (a.getNome().toLowerCase().contains(texto.toLowerCase())) {
                filtrada.add(a);
            }
        }
        adapter.atualizarLista(filtrada);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlunoController controller = new AlunoController(this);
        listaCompleta = controller.getAlunos();
        adapter.atualizarLista(listaCompleta);
    }
}