package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Answer parcelable
 */
public class Answer implements Parcelable {

    private String text;

    public Answer() {
        // Empty constructor needed for retrofit
    }

    public Answer(String text) {
        this.text = text;
    }

    public Answer(Parcel source) {
        this.text = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        public Answer createFromParcel(Parcel data) {
            return new Answer(data);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
