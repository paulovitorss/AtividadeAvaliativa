package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.model.SubjectGrade;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GradeActivity extends AppCompatActivity {

    private SubjectGradeAdapter subjectGradeAdapter;
    private List<Grade> allGrades = new ArrayList<>();
    private LinearLayout llHeaderGrades;
    private Spinner spinnerTerm;
    private RecyclerView rvNotas;
    private ProgressBar progressBar;

    private GradeViewModel gradeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        initViews();

        gradeViewModel = new ViewModelProvider(this).get(GradeViewModel.class);

        // A Activity agora recebe o ID do estudante da tela anterior.
        String userIdString = getIntent().getStringExtra("USER_ID_EXTRA");
        if (userIdString == null || userIdString.isEmpty()) {
            handleInvalidSession("ID do estudante não fornecido.");
            return;
        }

        UUID studentId;
        try {
            studentId = UUID.fromString(userIdString);
        } catch (IllegalArgumentException e) {
            handleInvalidSession("ID do estudante inválido.");
            return;
        }

        setupRecyclerView();
        observeGradeState();

        // Inicia a busca pelos dados
        gradeViewModel.fetchGrades(studentId);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    private void initViews() {
        spinnerTerm = findViewById(R.id.spinnerTerm);
        rvNotas = findViewById(R.id.recyclerViewNotas);
        llHeaderGrades = findViewById(R.id.llHeaderGrades);
        progressBar = findViewById(R.id.progressBar); // Assumindo que você tenha um progressBar no XML
    }

    private void setupRecyclerView() {
        subjectGradeAdapter = new SubjectGradeAdapter();
        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        rvNotas.setAdapter(subjectGradeAdapter);
    }

    private void observeGradeState() {
        gradeViewModel.gradeUiState.observe(this, state -> {
            setLoading(state instanceof GradeViewModel.GradeUiState.Loading);

            if (state instanceof GradeViewModel.GradeUiState.Success) {
                List<Grade> grades = ((GradeViewModel.GradeUiState.Success) state).grades;
                if (grades.isEmpty()) {
                    Toast.makeText(this, "Nenhuma nota encontrada", Toast.LENGTH_SHORT).show();
                } else {
                    allGrades = grades;
                    setupSpinner(grades);
                }
            } else if (state instanceof GradeViewModel.GradeUiState.Error) {
                String message = ((GradeViewModel.GradeUiState.Error) state).message;
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        spinnerTerm.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        rvNotas.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void setupSpinner(List<Grade> grades) {
        TreeSet<Integer> terms = new TreeSet<>();
        for (Grade g : grades) terms.add(g.getTerm());

        List<String> labels = new ArrayList<>();
        for (Integer t : terms) {
            labels.add(t + "º Bimestre");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, labels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerm.setAdapter(adapter);

        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int selectedTerm = new ArrayList<>(terms).get(pos);
                onTermSelected(selectedTerm);
            }
        });

        if (!terms.isEmpty()) {
            spinnerTerm.setSelection(0);
        }
    }

    private void onTermSelected(int term) {
        List<Grade> filtered = new ArrayList<>();
        for (Grade g : allGrades) {
            if (g.getTerm() == term) filtered.add(g);
        }
        setupHeader(filtered);
        List<SubjectGrade> list = new ArrayList<>();
        Map<String, List<BigDecimal>> map = new LinkedHashMap<>();
        for (Grade g : filtered) {
            String subj = g.getSubject().getName();
            map.computeIfAbsent(subj, k -> new ArrayList<>()).add(g.getGradeValue());
        }
        for (Map.Entry<String, List<BigDecimal>> e : map.entrySet()) {
            list.add(new SubjectGrade(e.getKey(), e.getValue()));
        }
        subjectGradeAdapter.submitList(list);
    }

    private void setupHeader(List<Grade> filtered) {
        Map<String, List<BigDecimal>> map = new LinkedHashMap<>();
        for (Grade g : filtered) {
            map.computeIfAbsent(g.getSubject().getName(), k -> new ArrayList<>())
                    .add(g.getGradeValue());
        }
        int maxNotas = 0;
        for (List<BigDecimal> l : map.values()) {
            if (l.size() > maxNotas) maxNotas = l.size();
        }
        llHeaderGrades.removeAllViews();
        for (int i = 1; i <= maxNotas; i++) {
            TextView tv = new TextView(this);
            tv.setText("Nota " + i);
            tv.setTextSize(16);
            tv.setPadding(24, 0, 24, 0);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            llHeaderGrades.addView(tv);
        }
    }

    private void handleInvalidSession(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}