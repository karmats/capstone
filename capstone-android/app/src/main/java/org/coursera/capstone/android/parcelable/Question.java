package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Question parcelable
 */
public class Question implements Parcelable {

    private String text;
    private List<Answer> answers;

    public Question() {
        // Empty constructor needed for retrofit
    }

    public Question(Parcel source) {
        this.text = source.readString();
        source.readTypedList(answers, Answer.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeTypedList(answers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel data) {
            return new Question(data);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
