package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
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
    private final MutableLiveData<ScreenState> _uiState = new MutableLiveData<>(ScreenState.initial());
    public final LiveData<ScreenState> uiState = _uiState;

    private UUID studentId;

    @Inject
    public AttendanceViewModel(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void setStudentIdAndFetchYears(UUID studentId) {
        this.studentId = studentId;
        fetchAvailableYears();
    }

    public void fetchAvailableYears() {
        if (studentId == null) {
            _uiState.setValue(ScreenState.error("ID do estudante não encontrado."));
            return;
        }

        _uiState.setValue(ScreenState.loadingInitial());

        attendanceRepository.getAttendanceYears(studentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Integer>> call, @NonNull Response<List<Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    var years = response.body();
                    if (!years.isEmpty()) {
                        fetchAttendanceForYear(years.get(0), years);
                    } else {
                        _uiState.postValue(ScreenState.success(Collections.emptyList(), Collections.emptyList()));
                    }
                } else {
                    _uiState.postValue(ScreenState.error("Falha ao buscar anos disponíveis."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Integer>> call, @NonNull Throwable t) {
                _uiState.postValue(ScreenState.error("Erro de conexão ao buscar anos."));
            }
        });
    }

    public void fetchAttendanceForYear(Integer year, List<Integer> availableYears) {
        if (studentId == null) return;

        _uiState.setValue(Objects.requireNonNull(uiState.getValue()).loadingSummaries());

        attendanceRepository.getAttendanceForStudent(studentId, year).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Attendance>> call, @NonNull Response<List<Attendance>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    var summaryList = processAttendances(response.body());
                    _uiState.postValue(ScreenState.success(availableYears, summaryList));
                } else {
                    _uiState.postValue(Objects.requireNonNull(uiState.getValue()).withError("Falha ao carregar frequências."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Attendance>> call, @NonNull Throwable t) {
                _uiState.postValue(Objects.requireNonNull(uiState.getValue()).withError("Erro de conexão."));
            }
        });
    }

    private List<AttendanceSummary> processAttendances(List<Attendance> records) {
        if (records == null || records.isEmpty()) return Collections.emptyList();

        Map<UUID, List<Attendance>> recordsBySubject = records.stream()
                .filter(r -> r.getSubject() != null && r.getSubject().getSubjectId() != null)
                .collect(Collectors.groupingBy(record -> record.getSubject().getSubjectId()));

        return recordsBySubject.values().stream()
                .map(subjectRecords -> {
                    if (subjectRecords.isEmpty()) return null;

                    var firstRecord = subjectRecords.get(0);
                    var subjectName = firstRecord.getSubject().getName();
                    int totalClasses = subjectRecords.size();
                    long presentClasses = subjectRecords.stream().filter(r -> "PRESENT".equalsIgnoreCase(r.getStatus())).count();

                    return new AttendanceSummary(subjectName, (int) presentClasses, totalClasses);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public record ScreenState(
            boolean isLoadingInitial,
            boolean isLoadingSummaries,
            @Nullable List<Integer> availableYears,
            @Nullable List<AttendanceSummary> attendanceSummaries,
            @Nullable String error
    ) {
        public static ScreenState initial() {
            return new ScreenState(true, false, null, null, null);
        }

        public static ScreenState loadingInitial() {
            return new ScreenState(true, false, null, null, null);
        }

        public ScreenState loadingSummaries() {
            return new ScreenState(false, true, this.availableYears, this.attendanceSummaries, null);
        }

        public static ScreenState success(List<Integer> years, List<AttendanceSummary> summaries) {
            return new ScreenState(false, false, years, summaries, null);
        }

        public static ScreenState error(String message) {
            return new ScreenState(false, false, null, null, message);
        }

        public ScreenState withError(String message) {
            return new ScreenState(false, false, this.availableYears, this.attendanceSummaries, message);
        }
    }
}