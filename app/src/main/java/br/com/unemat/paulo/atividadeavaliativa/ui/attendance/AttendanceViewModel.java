package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Attendance;
import br.com.unemat.paulo.atividadeavaliativa.data.model.AttendanceSummary;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.AttendanceRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AttendanceViewModel extends ViewModel {

    private final AttendanceRepository attendanceRepository;
    private final MutableLiveData<ScreenState> _uiState = new MutableLiveData<>();
    public final LiveData<ScreenState> uiState = _uiState;

    @Inject
    public AttendanceViewModel(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void fetchAvailableYears(UUID studentId) {
        _uiState.setValue(new ScreenState(true, false, Collections.emptyList(), null, null));

        attendanceRepository.getAttendanceYears(studentId).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Integer>> call, @NonNull Response<List<Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Integer> years = response.body();
                    if (!years.isEmpty()) {
                        fetchAttendanceForYear(studentId, years.get(0), years);
                    } else {
                        _uiState.postValue(new ScreenState(false, false, Collections.emptyList(), Collections.emptyList(), null));
                    }
                } else {
                    _uiState.postValue(new ScreenState(false, false, null, null, "Falha ao buscar anos disponíveis."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Integer>> call, @NonNull Throwable t) {
                _uiState.postValue(new ScreenState(false, false, null, null, "Erro de conexão ao buscar anos."));
            }
        });
    }

    public void fetchAttendanceForYear(UUID studentId, Integer year, List<Integer> availableYears) {
        _uiState.setValue(new ScreenState(false, true, availableYears, null, null));

        attendanceRepository.getAttendanceForStudent(studentId, year).enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(@NonNull Call<List<Attendance>> call, @NonNull Response<List<Attendance>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AttendanceSummary> summaryList = processAttendances(response.body());
                    _uiState.postValue(new ScreenState(false, false, availableYears, summaryList, null));
                } else {
                    _uiState.postValue(new ScreenState(false, false, availableYears, null, "Falha ao carregar frequências."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Attendance>> call, @NonNull Throwable t) {
                _uiState.postValue(new ScreenState(false, false, availableYears, null, "Erro de conexão."));
            }
        });
    }

    private List<AttendanceSummary> processAttendances(List<Attendance> records) {
        if (records == null || records.isEmpty()) return new ArrayList<>();
        Map<UUID, List<Attendance>> recordsBySubject = records.stream()
                .filter(r -> r.getSubject() != null)
                .collect(Collectors.groupingBy(record -> record.getSubject().getSubjectId()));
        List<AttendanceSummary> summaries = new ArrayList<>();
        for (List<Attendance> subjectRecords : recordsBySubject.values()) {
            if (subjectRecords.isEmpty()) continue;
            String subjectName = subjectRecords.get(0).getSubject().getName();
            AttendanceSummary summary = new AttendanceSummary(subjectName);
            for (Attendance record : subjectRecords) {
                summary.incrementTotal();
                if ("PRESENT".equalsIgnoreCase(record.getStatus())) summary.incrementPresence();
            }
            summaries.add(summary);
        }
        return summaries;
    }

    public static class ScreenState {
        public final boolean isLoadingInitial;
        public final boolean isLoadingSummaries;
        public final List<Integer> availableYears;
        public final List<AttendanceSummary> attendanceSummaries;
        public final String error;

        public ScreenState(boolean isLoadingInitial, boolean isLoadingSummaries, List<Integer> availableYears, List<AttendanceSummary> attendanceSummaries, String error) {
            this.isLoadingInitial = isLoadingInitial;
            this.isLoadingSummaries = isLoadingSummaries;
            this.availableYears = availableYears;
            this.attendanceSummaries = attendanceSummaries;
            this.error = error;
        }
    }
}