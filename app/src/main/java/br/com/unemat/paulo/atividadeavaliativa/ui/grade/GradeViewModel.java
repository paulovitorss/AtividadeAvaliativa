package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.GradeRepository;

public class GradeViewModel extends AndroidViewModel {
    private final GradeRepository gradeRepository;

    public GradeViewModel(@NonNull Application application) {
        super(application);
        this.gradeRepository = new GradeRepository(application.getApplicationContext());
    }

    public LiveData<List<Grade>> getGrades(UUID studentId) {
        return gradeRepository.getGrades(studentId);
    }
}