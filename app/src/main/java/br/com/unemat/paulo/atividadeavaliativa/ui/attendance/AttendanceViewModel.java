package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
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

    private final MutableLiveData<AttendanceUiState> _uiState = new MutableLiveData<>();
    public final LiveData<AttendanceUiState> uiState = _uiState;

    @Inject
    public AttendanceViewModel(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void fetchAttendance(UUID studentId) {
        _uiState.setValue(new AttendanceUiState.Loading());

        attendanceRepository.getAttendanceForStudent(studentId).enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(@NonNull Call<List<Attendance>> call, @NonNull Response<List<Attendance>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AttendanceSummary> summaryList = processAttendances(response.body());
                    _uiState.postValue(new AttendanceUiState.Success(summaryList));
                } else {
                    _uiState.postValue(new AttendanceUiState.Error("Falha ao carregar frequências."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Attendance>> call, @NonNull Throwable t) {
                _uiState.postValue(new AttendanceUiState.Error("Erro de conexão."));
            }
        });
    }

    private List<AttendanceSummary> processAttendances(List<Attendance> records) {
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }

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
                if ("PRESENT".equalsIgnoreCase(record.getStatus())) {
                    summary.incrementPresence();
                }
            }
            summaries.add(summary);
        }
        return summaries;
    }

    public static abstract class AttendanceUiState {
        private AttendanceUiState() {
        }

        public static final class Loading extends AttendanceUiState {
        }

        public static final class Success extends AttendanceUiState {
            public final List<AttendanceSummary> summaries;

            public Success(List<AttendanceSummary> summaries) {
                this.summaries = summaries;
            }
        }

        public static final class Error extends AttendanceUiState {
            public final String message;

            public Error(String message) {
                this.message = message;
            }
        }
    }
}