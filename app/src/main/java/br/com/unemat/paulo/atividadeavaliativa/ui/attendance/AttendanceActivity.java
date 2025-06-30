package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequencia);

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        initViews();
        setupRecyclerView();
        observeViewModel();

        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        if (studentId != null) {
            viewModel.fetchAttendance(studentId);
        } else {
            Toast.makeText(this, "ID do estudante não fornecido.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        contentGroup = findViewById(R.id.content_group);

        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance);
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        frequenciaAdapter = new AttendanceAdapter();
        recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAttendance.setAdapter(frequenciaAdapter);
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        contentGroup.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void observeViewModel() {
        viewModel.uiState.observe(this, state -> {
            setLoading(state instanceof AttendanceViewModel.AttendanceUiState.Loading);

            if (state instanceof AttendanceViewModel.AttendanceUiState.Success) {
                frequenciaAdapter.submitList(((AttendanceViewModel.AttendanceUiState.Success) state).summaries);
            } else if (state instanceof AttendanceViewModel.AttendanceUiState.Error) {
                String errorMessage = ((AttendanceViewModel.AttendanceUiState.Error) state).message;
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void start(Context context, UUID studentId) {
        Intent starter = new Intent(context, AttendanceActivity.class);
        starter.putExtra(EXTRA_STUDENT_ID, studentId);
        context.startActivity(starter);
    }
}