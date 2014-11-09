package org.coursera.capstone.android.parceable;

/**
 * A simple object to represent a patient.
 */
public class Patient {

    private String username;
    private Long medicalRecordNumber;
    private String firstName;
    private String lastName;
    private Long birthDate;

    private Doctor doctor;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(Long medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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