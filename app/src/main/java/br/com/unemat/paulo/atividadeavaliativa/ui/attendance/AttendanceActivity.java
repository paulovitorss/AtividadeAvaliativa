package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AttendanceActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID";
    private AttendanceViewModel viewModel;
    private AttendanceAdapter frequenciaAdapter;
    private RecyclerView recyclerViewAttendance;
    private ProgressBar progressBar;
    private Group contentGroup;
    private Spinner spinnerYear;
    private ArrayAdapter<Integer> spinnerAdapter;
    private UUID studentId;
    private boolean isUserInteraction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequencia);

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);

        initViews();
        setupRecyclerView();
        setupYearSpinner();
        observeViewModel();

        if (studentId != null) {
            viewModel.fetchAvailableYears(studentId);
        } else {
            Toast.makeText(this, "ID do estudante não fornecido.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        contentGroup = findViewById(R.id.content_group);
        spinnerYear = findViewById(R.id.spinnerYear);
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance);
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        frequenciaAdapter = new AttendanceAdapter();
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAttendance.setAdapter(frequenciaAdapter);
    }

    private void setupYearSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(spinnerAdapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isUserInteraction) return;

                Integer selectedYear = (Integer) parent.getItemAtPosition(position);
                AttendanceViewModel.ScreenState currentState = viewModel.uiState.getValue();

                if (studentId != null && currentState != null && currentState.availableYears != null) {
                    viewModel.fetchAttendanceForYear(studentId, selectedYear, currentState.availableYears);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void observeViewModel() {
        viewModel.uiState.observe(this, state -> {
            progressBar.setVisibility(state.isLoadingInitial || state.isLoadingSummaries ? View.VISIBLE : View.GONE);
            contentGroup.setVisibility(state.isLoadingInitial ? View.GONE : View.VISIBLE);
            recyclerViewAttendance.setVisibility(state.isLoadingSummaries ? View.INVISIBLE : View.VISIBLE);

            if (state.availableYears != null && spinnerAdapter.getCount() == 0 && !state.availableYears.isEmpty()) {
                isUserInteraction = false;
                spinnerAdapter.clear();
                spinnerAdapter.addAll(state.availableYears);
                spinnerAdapter.notifyDataSetChanged();
                isUserInteraction = true;
            }
            spinnerYear.setVisibility(state.availableYears != null && !state.availableYears.isEmpty() ? View.VISIBLE : View.GONE);

            if (state.attendanceSummaries != null) {
                frequenciaAdapter.submitList(state.attendanceSummaries);
            }

            if (state.error != null) {
                Toast.makeText(this, state.error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void start(Context context, UUID studentId) {
        Intent starter = new Intent(context, AttendanceActivity.class);
        starter.putExtra(EXTRA_STUDENT_ID, studentId);
        context.startActivity(starter);
    }
}