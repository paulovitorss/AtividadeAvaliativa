package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.databinding.ActivityAttendanceBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AttendanceActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID";
    private ActivityAttendanceBinding binding;
    private AttendanceAdapter attendanceAdapter;
    private AttendanceViewModel viewModel;

    public static void start(Context context, UUID studentId) {
        Intent starter = new Intent(context, AttendanceActivity.class);
        starter.putExtra(EXTRA_STUDENT_ID, studentId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupYearSelector();
        setupHeader();
        observeViewModel();

        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        if (studentId != null) {
            viewModel.setStudentIdAndFetchYears(studentId);
        } else {
            Toast.makeText(this, R.string.error_student_id_not_provided, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        attendanceAdapter = new AttendanceAdapter();
        binding.recyclerViewAttendance.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewAttendance.setAdapter(attendanceAdapter);
    }

    private void setupYearSelector() {
        if (binding.menuYear.getEditText() instanceof AutoCompleteTextView auto) {
            auto.setOnItemClickListener((parent, view, position, id) -> {
                Integer year = (Integer) parent.getAdapter().getItem(position);
                var state = viewModel.uiState.getValue();
                if (state != null && state.availableYears() != null) {
                    viewModel.fetchAttendanceForYear(year, state.availableYears());
                }
            });
        }
    }

    private void setupHeader() {
        var header = binding.headerRow;

        header.txtDisciplina.setText(R.string.header_disciplina);
        header.txtTotalAulas.setText(R.string.header_total);
        header.txtPresencas.setText(R.string.header_presencas);
        header.txtPercentual.setText(R.string.header_percentual);

        header.txtDisciplina.setTypeface(null, Typeface.BOLD);
        header.txtTotalAulas.setTypeface(null, Typeface.BOLD);
        header.txtPresencas.setTypeface(null, Typeface.BOLD);
        header.txtPercentual.setTypeface(null, Typeface.BOLD);
    }

    private void observeViewModel() {
        viewModel.uiState.observe(this, this::updateUi);
    }

    private void updateUi(@NonNull AttendanceViewModel.ScreenState state) {
        binding.progressBar.setVisibility(state.isLoadingInitial() ? View.VISIBLE : View.GONE);
        binding.recyclerViewAttendance.setVisibility(state.isLoadingInitial() ? View.GONE : View.VISIBLE);
        binding.btnVoltar.setVisibility(state.isLoadingInitial() ? View.GONE : View.VISIBLE);
        binding.menuYear.setVisibility(state.isLoadingInitial() ? View.GONE : View.VISIBLE);

        if (!state.isLoadingInitial()) {
            binding.recyclerViewAttendance.setAlpha(state.isLoadingSummaries() ? 0.5f : 1f);
        }

        List<Integer> years = state.availableYears() != null
                ? state.availableYears()
                : Collections.emptyList();

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                years
        );

        if (binding.menuYear.getEditText() instanceof AutoCompleteTextView auto) {
            auto.setAdapter(adapter);
            if (!years.isEmpty() && auto.getText().toString().isEmpty()) {
                auto.setText(String.valueOf(years.get(0)), false);
            }
        }

        attendanceAdapter.submitList(state.attendanceSummaries());

        if (state.error() != null && !state.error().isEmpty()) {
            Toast.makeText(this, state.error(), Toast.LENGTH_LONG).show();
        }
    }
}