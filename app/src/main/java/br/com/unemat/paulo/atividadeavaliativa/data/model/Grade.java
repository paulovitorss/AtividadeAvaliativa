package br.com.unemat.paulo.atividadeavaliativa.data.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Grade {
    private UUID gradeId;
    private StudentInfo student;
    private SubjectInfo subject;
    private String academicYear;
    private Integer term;
    private BigDecimal gradeValue;

    public UUID getGradeId() {
        return gradeId;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public SubjectInfo getSubject() {
        return subject;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public Integer getTerm() {
        return term;
    }

    public BigDecimal getGradeValue() {
        return gradeValue;
    }

    public static class StudentInfo {
        private UUID userId;
        private String name;

        public UUID getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }
    }

    public static class SubjectInfo {
        private UUID subjectId;
        private String name;

        public UUID getSubjectId() {
            return subjectId;
        }

        public String getName() {
            return name;
        }
    }
}
