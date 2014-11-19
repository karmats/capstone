package org.coursera.capstone.dto;

import java.util.Date;
import java.util.List;

public class CheckInDto {

    private Long patientMedicalRecordNumber;
    private List<PatientAnswerDto> patientAnswers;
    private List<MedicationTakenDto> medicationsTaken;
    private Date when;

    public Long getPatientMedicalRecordNumber() {
        return patientMedicalRecordNumber;
    }

    public void setPatientMedicalRecordNumber(Long patientMedicalRecordNumber) {
        this.patientMedicalRecordNumber = patientMedicalRecordNumber;
    }

    public List<PatientAnswerDto> getPatientAnswers() {
        return patientAnswers;
    }

    public void setPatientAnswers(List<PatientAnswerDto> patientAnswers) {
        this.patientAnswers = patientAnswers;
    }

    public List<MedicationTakenDto> getMedicationsTaken() {
        return medicationsTaken;
    }

    public void setMedicationsTaken(List<MedicationTakenDto> medicationsTaken) {
        this.medicationsTaken = medicationsTaken;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    /**
     * Patient answers, id of the question and the answer the question submitted.
     * 
     * @author matros
     *
     */
    public static class PatientAnswerDto {

        private Long questionId;
        private Long answerId;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public Long getAnswerId() {
            return answerId;
        }

        public void setAnswerId(Long answerId) {
            this.answerId = answerId;
        }

    }

    /**
     * Medications taken. Id for the medication, boolean if the patient took it or not, and when the patient took it if
     * he/she did take it
     * 
     * @author matros
     *
     */
    public static class MedicationTakenDto {

        private String medicationId;
        private boolean taken;
        private Date when;

        public String getMedicationId() {
            return medicationId;
        }

        public void setMedicationId(String medicationId) {
            this.medicationId = medicationId;
        }

        public boolean isTaken() {
            return taken;
        }

        public void setTaken(boolean taken) {
            this.taken = taken;
        }

        public Date getWhen() {
            return when;
        }

        public void setWhen(Date when) {
            this.when = when;
        }

    }
}
