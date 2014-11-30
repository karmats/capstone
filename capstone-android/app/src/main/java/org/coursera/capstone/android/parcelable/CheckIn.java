package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import org.coursera.capstone.android.parcelable.Answer;
import org.coursera.capstone.android.parcelable.CheckInRequest;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describes a patient check in
 */
public class CheckIn implements Parcelable {

    private Patient patient;
    private List<QuestionAnswer> questionAnswers;
    private List<MedicationTaken> medicationsTaken;

    public CheckIn() {
        this.questionAnswers = new ArrayList<QuestionAnswer>();
        this.medicationsTaken = new ArrayList<MedicationTaken>();
    }

    public CheckIn(Parcel source) {
        this.patient = source.readParcelable(Patient.class.getClassLoader());
        source.readTypedList(this.questionAnswers, QuestionAnswer.CREATOR);
        source.readTypedList(this.medicationsTaken, MedicationTaken.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(patient, 0);
        dest.writeTypedList(questionAnswers);
        dest.writeTypedList(medicationsTaken);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public List<MedicationTaken> getMedicationsTaken() {
        return medicationsTaken;
    }

    public void setMedicationsTaken(List<MedicationTaken> medicationsTaken) {
        this.medicationsTaken = medicationsTaken;
    }

    /**
     * Creates a @link{CheckInRequest} based on values in this check in
     *
     * @return @link{CheckInRequest}
     */
    public CheckInRequest toRequest() {
        CheckInRequest request = new CheckInRequest();
        request.setPatientMedicalRecordNumber(patient.getMedicalRecordNumber());
        // Questions
        for (QuestionAnswer questionAnswer : questionAnswers) {
            request.getPatientAnswers().add(new CheckInRequest.PatientAnswerRequest(questionAnswer.getQuestion().getId(),
                    questionAnswer.getAnswer().getId()));
        }
        // Medications
        for (MedicationTaken medication : medicationsTaken) {
            request.getMedicationsTaken().add(new CheckInRequest.MedicationTakenRequest(
                    medication.getMedication().getMedicationId(), medication.getWhen() != null,
                    medication.getWhen() != null ? medication.getWhen() : null));
        }
        // Complete the request by setting the when to now
        request.setWhen(new Date().getTime());
        return request;
    }

    /**
     * Patient answers, a question and its answer.
     */
    public static class QuestionAnswer implements Parcelable {
        private Question question;
        private Answer answer;

        public QuestionAnswer() {
        }

        public QuestionAnswer(Question question, Answer answer) {
            this.question = question;
            this.answer = answer;
        }

        public QuestionAnswer(Parcel source) {
            this.question = source.readParcelable(Question.class.getClassLoader());
            this.answer = source.readParcelable(Answer.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(question, 0);
            dest.writeParcelable(answer, 0);

        }

        public static final Parcelable.Creator<QuestionAnswer> CREATOR = new Parcelable.Creator<QuestionAnswer>() {
            public QuestionAnswer createFromParcel(Parcel data) {
                return new QuestionAnswer(data);
            }

            public QuestionAnswer[] newArray(int size) {
                return new QuestionAnswer[size];
            }
        };

        public Question getQuestion() {
            return question;
        }


        public void setQuestion(Question question) {
            this.question = question;
        }

        public Answer getAnswer() {
            return answer;
        }

        public void setAnswer(Answer answer) {
            this.answer = answer;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof QuestionAnswer) {
                QuestionAnswer that = (QuestionAnswer) o;
                return that.getQuestion().getId().equals(this.getQuestion().getId());
            }
            return super.equals(o);
        }
    }

    /**
     * Medications taken. PainMedication patient took and when in milliseconds he/she did its
     */
    public static class MedicationTaken implements Parcelable {

        private PainMedication medication;
        private Long when;

        public MedicationTaken() {
        }

        public MedicationTaken(PainMedication medication, Long when) {
            this.medication = medication;
            this.when = when;
        }

        public MedicationTaken(Parcel source) {
            this.medication = source.readParcelable(PainMedication.class.getClassLoader());
            this.when = source.readLong();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(medication, 0);
            if (when != null) {
                dest.writeLong(when);
            }
        }

        public static final Parcelable.Creator<MedicationTaken> CREATOR = new Parcelable.Creator<MedicationTaken>() {
            public MedicationTaken createFromParcel(Parcel data) {
                return new MedicationTaken(data);
            }

            public MedicationTaken[] newArray(int size) {
                return new MedicationTaken[size];
            }
        };

        public PainMedication getMedication() {
            return medication;
        }

        public void setMedication(PainMedication medication) {
            this.medication = medication;
        }

        public Long getWhen() {
            return when;
        }

        public void setWhen(Long when) {
            this.when = when;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof MedicationTaken) {
                MedicationTaken that = (MedicationTaken) o;
                return that.getMedication().getMedicationId().equals(this.getMedication().getMedicationId());
            }
            return super.equals(o);
        }
    }
}
