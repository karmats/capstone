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
public class Patient extends UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long medicalRecordNumber;
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
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public Patient(String username, long medicalRecordNumber, String firstName, String lastName, Date birthDate,
            Doctor doctor) {
        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setMedicalRecordNumber(medicalRecordNumber);
        this.setBirthDate(birthDate);
        this.setDoctor(doctor);
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
        return Objects.hashCode(this.getFirstName(), this.getLastName(), this.getMedicalRecordNumber());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Patient) {
            Patient other = (Patient) obj;
            return Objects.equal(this.getFirstName(), other.getFirstName())
                    && Objects.equal(this.getLastName(), other.getLastName())
                    && this.getMedicalRecordNumber() == other.getMedicalRecordNumber();
        } else {
            return false;
        }
    }
}
