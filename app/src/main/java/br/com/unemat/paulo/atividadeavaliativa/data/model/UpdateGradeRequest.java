package br.com.unemat.paulo.atividadeavaliativa.data.model;

import java.math.BigDecimal;

public class UpdateGradeRequest {
    private BigDecimal gradeValue;

    public BigDecimal getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(BigDecimal gradeValue) {
        this.gradeValue = gradeValue;
    }
}