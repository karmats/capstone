package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple object to represent a patient.
 */
public class Patient extends User {

    private Long medicalRecordNumber;
    // Date as ms
    private Long birthDate;

    private Doctor doctor;

    public Patient() {
        super();
        // Empty constructor needed for retrofits
    }

    public Patient(Parcel source) {
        super(source);
        source.writeLong(medicalRecordNumber);
        source.writeLong(birthDate);
        source.writeParcelable(doctor, 0);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(medicalRecordNumber);
        dest.writeLong(birthDate);
    }

    public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
        public Patient createFromParcel(Parcel data) {
            return new Patient(data);
        }

        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public Long getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(Long medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

}