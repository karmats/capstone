package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Response for a patient check-in
 */
public class CheckInResponse implements Parcelable {

    private List<QuestionAnswerResponse> questionAnswers;
    private List<MedicationTaken> medicationsTaken;
    private List<String> alerts;
    private Long when;

    public CheckInResponse() {
    }

    public CheckInResponse(Parcel source) {
        source.readTypedList(this.questionAnswers, QuestionAnswerResponse.CREATOR);
        source.readTypedList(this.medicationsTaken, MedicationTaken.CREATOR);
        source.readStringList(this.alerts);
        this.when = source.readLong();
    }

    public static final Parcelable.Creator<CheckInResponse> CREATOR = new Parcelable.Creator<CheckInResponse>() {
        public CheckInResponse createFromParcel(Parcel data) {
            return new CheckInResponse(data);
        }

        public CheckInResponse[] newArray(int size) {
            return new CheckInResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.questionAnswers);
        parcel.writeTypedList(this.medicationsTaken);
        parcel.writeStringList(this.alerts);
        parcel.writeLong(this.when);
    }


    public List<QuestionAnswerResponse> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswerResponse> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public List<MedicationTaken> getMedicationsTaken() {
        return medicationsTaken;
    }

    public void setMedicationsTaken(List<MedicationTaken> medicationsTaken) {
        this.medicationsTaken = medicationsTaken;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }

    public Long getWhen() {
        return when;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(this.when)) + " Questions: " + this.questionAnswers.size() +
                " Medications taken: " + this.medicationsTaken.size();
    }

    /**
     * Patient questions and answers as text.
     */
    public static class QuestionAnswerResponse implements Parcelable {

        private String questionText;
        private String answerText;

        public QuestionAnswerResponse() {
        }

        public QuestionAnswerResponse(Parcel source) {
            this.questionText = source.readString();
            this.answerText = source.readString();
        }

        public static final Parcelable.Creator<QuestionAnswerResponse> CREATOR = new Parcelable.Creator<QuestionAnswerResponse>() {
            public QuestionAnswerResponse createFromParcel(Parcel data) {
                return new QuestionAnswerResponse(data);
            }

            public QuestionAnswerResponse[] newArray(int size) {
                return new QuestionAnswerResponse[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(questionText);
            parcel.writeString(answerText);
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
     * Medications taken. Name of the medication and when the patient took it.
     */
    public static class MedicationTaken implements Parcelable {

        private String medicationName;
        private Long when;

        public MedicationTaken() {
        }

        public MedicationTaken(Parcel source) {
            this.medicationName = source.readString();
            this.when = source.readLong();
        }

        public static final Parcelable.Creator<MedicationTaken> CREATOR = new Parcelable.Creator<MedicationTaken>() {
            public MedicationTaken createFromParcel(Parcel data) {
                return new MedicationTaken(data);
            }

            public MedicationTaken[] newArray(int size) {
                return new MedicationTaken[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(medicationName);
            parcel.writeLong(when);
        }

        public String getMedicationName() {
            return medicationName;
        }

        public void setMedicationName(String medicationName) {
            this.medicationName = medicationName;
        }

        public Long getWhen() {
            return when;
        }

        public void setWhen(Long when) {
            this.when = when;
        }

    }
}
