package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class UpdateGradeRequest {
    @SerializedName("grade_value")
    private BigDecimal gradeValue;

    public BigDecimal getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(BigDecimal gradeValue) {
        this.gradeValue = gradeValue;
    }
}