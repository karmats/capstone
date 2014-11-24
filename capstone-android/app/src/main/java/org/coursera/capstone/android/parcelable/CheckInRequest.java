package org.coursera.capstone.android.parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a patient check-in
 */
public class CheckInRequest {

    private Long patientMedicalRecordNumber;
    private List<PatientAnswer> patientAnswers;
    private List<MedicationTaken> medicationsTaken;
    private Long when;

    public CheckInRequest() {
        this.patientAnswers = new ArrayList<PatientAnswer>();
        this.medicationsTaken = new ArrayList<MedicationTaken>();
    }

    public Long getPatientMedicalRecordNumber() {
        return patientMedicalRecordNumber;
    }

    public void setPatientMedicalRecordNumber(Long patientMedicalRecordNumber) {
        this.patientMedicalRecordNumber = patientMedicalRecordNumber;
    }

    public List<PatientAnswer> getPatientAnswers() {
        return patientAnswers;
    }

    public void setPatientAnswers(List<PatientAnswer> patientAnswers) {
        this.patientAnswers = patientAnswers;
    }

    public List<MedicationTaken> getMedicationsTaken() {
        return medicationsTaken;
    }

    public void setMedicationsTaken(List<MedicationTaken> medicationsTaken) {
        this.medicationsTaken = medicationsTaken;
    }

    public Long getWhen() {
        return when;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    /**
     * Patient answers, id of the question and the answer the question submitted.
     */
    public static class PatientAnswer {

        private Long questionId;
        private Long answerId;

        public PatientAnswer() {
        }

        public PatientAnswer(Long questionId, Long answerId) {
            this.questionId = questionId;
            this.answerId = answerId;
        }

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
     */
    public static class MedicationTaken {

        private String medicationId;
        private boolean taken;
        private Long when;

        public MedicationTaken() {
        }

        public MedicationTaken(String medicationId, boolean taken, Long when) {
            this.medicationId = medicationId;
            this.taken = taken;
            this.when = when;
        }

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

        public Long getWhen() {
            return when;
        }

        public void setWhen(Long when) {
            this.when = when;
        }

    }
}
