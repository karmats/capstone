package org.coursera.capstone.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A simple object to represent a patient.
 * 
 */
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long medicalRecordNumber;
    private String firstName;
    private String lastName;
    private Date birthDate;

    private Doctor doctor;
    private List<PainMedication> painMedications;

    public Patient() {
    }

    public Patient(String firstName) {
        super();
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(long medicalRecordNumber) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<PainMedication> getPainMedications() {
        return painMedications;
    }

    public void setPainMedications(List<PainMedication> painMedications) {
        this.painMedications = painMedications;
    }

}
