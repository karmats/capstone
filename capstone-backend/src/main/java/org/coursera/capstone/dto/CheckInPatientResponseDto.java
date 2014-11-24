package org.coursera.capstone.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.entity.Answer;
import org.coursera.capstone.entity.CheckIn;
import org.coursera.capstone.entity.PatientAnswer;
import org.coursera.capstone.entity.PatientMedicationTaken;
import org.coursera.capstone.entity.Question;

/**
 * Response for a patient check-in
 * 
 * @author matros
 *
 */
public class CheckInPatientResponseDto {

    private List<QuestionAnswerDto> questionAnswers;
    private List<MedicationTakenDto> medicationsTaken;
    private Date when;

    public CheckInPatientResponseDto() {
    }

    public CheckInPatientResponseDto(CheckIn checkInEntity) {
        this.when = checkInEntity.getCheckInTime();
        // Question answers
        this.questionAnswers = new ArrayList<CheckInPatientResponseDto.QuestionAnswerDto>();
        for (PatientAnswer pa : checkInEntity.getPatientAnswers()) {
            this.questionAnswers.add(new QuestionAnswerDto(pa.getQuestion(), pa.getAnswer()));
        }
        // Medications taken
        this.medicationsTaken = new ArrayList<MedicationTakenDto>();
        for (PatientMedicationTaken pmt : checkInEntity.getPatientMedicationsTaken()) {
            this.medicationsTaken.add(new MedicationTakenDto(pmt));
        }

    }
    public List<QuestionAnswerDto> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswerDto> questionAnswers) {
        this.questionAnswers = questionAnswers;
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
     * Patient questions and answers as text.
     * 
     * @author matros
     *
     */
    public static class QuestionAnswerDto {

        private String questionText;
        private String answerText;

        public QuestionAnswerDto() {
        }

        public QuestionAnswerDto(Question question, Answer answer) {
            this.questionText = question.getText();
            this.answerText = answer.getText();
        }

        public String getQuestionText() {
            return questionText;
        }
        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }
        public String getAnswerText() {
            return answerText;
        }
        public void setAnswerText(String answerText) {
            this.answerText = answerText;
        }
    }

    /**
     * Medications taken. Name of the medication and when the patient took it if he/she did take it.
     * 
     * @author matros
     */
    public static class MedicationTakenDto {

        private String medicationName;
        // Might be null if patient didn't take this medication
        private Date when;

        public MedicationTakenDto() {
        }

        public MedicationTakenDto(PatientMedicationTaken medicationTaken) {
            this.medicationName = medicationTaken.getMedication().getName();
            this.when = medicationTaken.getMedicationTime();
        }

        public String getMedicationName() {
            return medicationName;
        }
        public void setMedicationName(String medicationName) {
            this.medicationName = medicationName;
        }
        public Date getWhen() {
            return when;
        }
        public void setWhen(Date when) {
            this.when = when;
        }

    }
}
