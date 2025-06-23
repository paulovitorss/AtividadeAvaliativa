package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.security.JwtUtils;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;

public class GradeActivity extends AppCompatActivity {

    private GradeAdapter gradeAdapter;
    private List<Grade> allGrades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Spinner spinnerTerm = findViewById(R.id.spinnerTerm);
        RecyclerView rvNotas = findViewById(R.id.recyclerViewNotas);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        gradeAdapter = new GradeAdapter();

        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        rvNotas.setAdapter(gradeAdapter);

        TokenManager tm = TokenManager.getInstance(this);
        String token = tm.getTokenSync();
        UUID studentId = JwtUtils.getUserIdFromToken(token);
        if (studentId == null) {
            Toast.makeText(this, "Sessão inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        GradeViewModel vm = new ViewModelProvider(this).get(GradeViewModel.class);
        vm.getGrades(studentId).observe(this, grades -> {
            if (grades == null || grades.isEmpty()) {
                Toast.makeText(this, "Nenhuma nota", Toast.LENGTH_SHORT).show();
                return;
            }
            allGrades = grades;
            setupSpinner(spinnerTerm, grades);
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void setupSpinner(Spinner spinner, List<Grade> grades) {
        // extrai termos únicos e ordena
        TreeSet<Integer> terms = new TreeSet<>();
        for (Grade g : grades) terms.add(g.getTerm());

        List<String> items = new ArrayList<>();
        for (Integer t : terms) {
            items.add(t + "º Bimestre");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int selectedTerm = new ArrayList<>(terms).get(pos);
                filterByTerm(selectedTerm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setSelection(0);
    }

    private void filterByTerm(int term) {
        List<Grade> filtered = new ArrayList<>();
        for (Grade g : allGrades) {
            if (g.getTerm() == term) filtered.add(g);
        }
        gradeAdapter.submitList(filtered);
    }
}