package org.coursera.capstone.android.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * A simple object to represent a patient.
 */
public class Patient extends User {

    private Long medicalRecordNumber;
    // Date as ms
    private Long birthDate;

    private Doctor doctor;
    private List<PainMedication> medications;

    public Patient() {
        super();
        // Empty constructor needed for retrofit
    }

    public Patient(Parcel source) {
        super(source);
        this.medicalRecordNumber = source.readLong();
        this.birthDate = source.readLong();
        this.doctor = source.readParcelable(Doctor.class.getClassLoader());
        source.readTypedList(medications, PainMedication.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(medicalRecordNumber);
        dest.writeLong(birthDate);
        dest.writeParcelable(doctor, 0);
        dest.writeTypedList(medications);
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

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<PainMedication> getMedications() {
        return medications;
    }

    public void setMedications(List<PainMedication> medications) {
        this.medications = medications;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Patient) {
            Patient that = (Patient) o;
            return that.getMedicalRecordNumber().equals(this.getMedicalRecordNumber());
        }
        return false;
    }
}