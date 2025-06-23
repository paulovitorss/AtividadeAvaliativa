package br.com.unemat.paulo.atividadeavaliativa.data.model;

import java.math.BigDecimal;
import java.util.List;

public class SubjectGrade {
    private final String subjectName;
    private final List<BigDecimal> grades;

    public SubjectGrade(String subjectName, List<BigDecimal> grades) {
        this.subjectName = subjectName;
        this.grades = grades;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public List<BigDecimal> getGrades() {
        return grades;
    }
}