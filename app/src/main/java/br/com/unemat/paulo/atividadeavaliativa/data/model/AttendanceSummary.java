package br.com.unemat.paulo.atividadeavaliativa.data.model;

import java.text.DecimalFormat;

public class AttendanceSummary {
    private final String subjectName;
    private int totalClasses;
    private int presentClasses;

    public AttendanceSummary(String subjectName) {
        this.subjectName = subjectName;
        this.totalClasses = 0;
        this.presentClasses = 0;
    }

    public void incrementTotal() {
        this.totalClasses++;
    }

    public void incrementPresence() {
        this.presentClasses++;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getPresentClasses() {
        return presentClasses;
    }

    public double getPercentage() {
        if (totalClasses == 0) {
            return 0.0;
        }
        return ((double) presentClasses / totalClasses) * 100.0;
    }

    public String getFormattedPercentage() {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(getPercentage());
    }
}