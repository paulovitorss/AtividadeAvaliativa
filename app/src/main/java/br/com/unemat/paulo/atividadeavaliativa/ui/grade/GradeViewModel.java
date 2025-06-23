package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.CreateGradeRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.model.UpdateGradeRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.GradeRepository;

public class GradeViewModel extends AndroidViewModel {

    private final GradeRepository gradeRepository;

    public GradeViewModel(@NonNull Application app) {
        super(app);
        gradeRepository = new GradeRepository(app);
    }

    public LiveData<List<Grade>> getGrades(UUID studentId) {
        return gradeRepository.getGrades(studentId);
    }

    public LiveData<Grade> createGrade(CreateGradeRequest request) {
        return gradeRepository.createGrade(request);
    }

    public LiveData<Grade> updateGrade(UUID gradeId, UpdateGradeRequest request) {
        return gradeRepository.updateGrade(gradeId, request);
    }

    public LiveData<Boolean> deleteGrade(UUID gradeId) {
        return gradeRepository.deleteGrade(gradeId);
    }
}