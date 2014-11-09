package org.coursera.capstone.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    @ManyToMany
    private Collection<PainMedication> painMedications;
    @OneToMany(mappedBy = "patient")
    private Collection<PatientAnswer> patientAnswers;

    public Patient() {
    }

    public Patient(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Patient(String username, long medicalRecordNumber, String firstName, String lastName, Date birthDate,
            Doctor doctor) {
        this.username = username;
        this.medicalRecordNumber = medicalRecordNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.doctor = doctor;
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

    public Collection<PainMedication> getPainMedications() {
        return painMedications;
    }

    public void setPainMedications(Collection<PainMedication> painMedications) {
        this.painMedications = painMedications;
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
