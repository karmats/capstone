package org.coursera.capstone.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.base.Objects;

/**
 * A simple object to represent a patient.
 * 
 */
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private long medicalRecordNumber;
    private String firstName;
    private String lastName;
    private Date birthDate;

    @ManyToOne
    private Doctor doctor;
    @OneToMany(mappedBy = "patient")
    private List<PatientPainMedication> patientPainMedications;

    public Patient() {
    }

    public Patient(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<PatientPainMedication> getPatientPainMedications() {
        return patientPainMedications;
    }

    public void setPatientPainMedications(List<PatientPainMedication> patientPainMedications) {
        this.patientPainMedications = patientPainMedications;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstName, lastName, medicalRecordNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Patient) {
            Patient other = (Patient) obj;
            return Objects.equal(this.firstName, other.firstName) && Objects.equal(this.lastName, other.lastName)
                    && this.medicalRecordNumber == other.medicalRecordNumber;
        } else {
            return false;
        }
    }
}
