package br.com.unemat.paulo.atividadeavaliativa.data.model;

import java.text.DecimalFormat;
import java.util.Objects;

public record AttendanceSummary(
        String subjectName,
        int presentClasses,
        int totalClasses
) {
    public AttendanceSummary {
        if (totalClasses < 0 || presentClasses < 0 || presentClasses > totalClasses) {
            throw new IllegalArgumentException("Valores de frequência inválidos.");
        }
    }

    public double getPercentage() {
        if (totalClasses == 0) {
            return 0.0;
        }
        return ((double) presentClasses / totalClasses) * 100.0;
    }

    public String getFormattedPercentage() {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(getPercentage()) + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceSummary that = (AttendanceSummary) o;
        return presentClasses == that.presentClasses &&
                totalClasses == that.totalClasses &&
                Objects.equals(subjectName, that.subjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectName, presentClasses, totalClasses);
    }
}