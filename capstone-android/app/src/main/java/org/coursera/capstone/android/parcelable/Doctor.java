package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple object to represent a doctor.
 */
public class Doctor extends User implements Parcelable {

    public Doctor() {
        super();
        // Empty constructor needed for retrofit
    }

    public Doctor(Parcel source) {
        super(source);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<Doctor> CREATOR = new Parcelable.Creator<Doctor>() {
        public Doctor createFromParcel(Parcel data) {
            return new Doctor(data);
        }

        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };
}
