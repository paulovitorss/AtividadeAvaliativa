package br.com.unemat.paulo.atividadeavaliativa.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Attendance {

    @SerializedName("attendanceRecordId")
    private UUID attendanceRecordId;

    @SerializedName("subject")
    private SubjectInfo subject;

    @SerializedName("status")
    private String status;

    public SubjectInfo getSubject() {
        return subject;
    }

    public String getStatus() {
        return status;
    }

    public static class SubjectInfo {
        @SerializedName("subjectId")
        private UUID subjectId;

        @SerializedName("name")
        private String name;

        public UUID getSubjectId() {
            return subjectId;
        }

        public String getName() {
            return name;
        }
    }
}