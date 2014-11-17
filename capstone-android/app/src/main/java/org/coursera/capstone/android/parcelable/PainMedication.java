package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple object to represent a pain medication.
 */
public class PainMedication implements Parcelable {

    private String name;
    private String medicationId;

    public PainMedication() {
        // Empty constructor needed for retrofit
    }

    public PainMedication(Parcel source) {
        this.name = source.readString();
        this.medicationId = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(medicationId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PainMedication> CREATOR = new Parcelable.Creator<PainMedication>() {
        public PainMedication createFromParcel(Parcel data) {
            return new PainMedication(data);
        }

        public PainMedication[] newArray(int size) {
            return new PainMedication[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
}
