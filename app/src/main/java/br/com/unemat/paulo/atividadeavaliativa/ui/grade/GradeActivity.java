package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import br.com.unemat.paulo.atividadeavaliativa.security.JwtUtils;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GradeActivity extends AppCompatActivity {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private SubjectGradeAdapter subjectGradeAdapter;
    private List<Grade> allGrades = new ArrayList<>();
    private LinearLayout llHeaderGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Spinner spinnerTerm = findViewById(R.id.spinnerTerm);
        RecyclerView rvNotas = findViewById(R.id.recyclerViewNotas);
        llHeaderGrades = findViewById(R.id.llHeaderGrades);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        subjectGradeAdapter = new SubjectGradeAdapter();
        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        rvNotas.setAdapter(subjectGradeAdapter);

        TokenManager tm = TokenManager.getInstance(this);
        GradeViewModel vm = new ViewModelProvider(this).get(GradeViewModel.class);

        disposables.add(tm.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    if (token.isEmpty()) {
                        handleInvalidSession();
                        return;
                    }

                    UUID studentId = JwtUtils.getUserIdFromToken(token);
                    if (studentId == null) {
                        handleInvalidSession();
                        return;
                    }

                    vm.getGrades(studentId).observe(this, grades -> {
                        if (grades == null || grades.isEmpty()) {
                            Toast.makeText(this, "Nenhuma nota encontrada", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        allGrades = grades;
                        setupSpinner(spinnerTerm, grades);
                    });

                }, throwable -> {
                    Log.e("GradeActivity", "Falha ao obter token", throwable);
                    handleInvalidSession();
                })
        );

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void handleInvalidSession() {
        Toast.makeText(this, "Sessão inválida. Por favor, faça login novamente.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    private void setupSpinner(Spinner spinner, List<Grade> grades) {
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
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int selectedTerm = new ArrayList<>(terms).get(pos);
                onTermSelected(selectedTerm);
            }
        });

        spinner.setSelection(0);
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
}