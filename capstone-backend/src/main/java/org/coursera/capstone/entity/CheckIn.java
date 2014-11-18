package org.coursera.capstone.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date when;

    @OneToMany(mappedBy = "answerCheckIn")
    private Collection<PatientAnswer> patientAnswers;
    @OneToMany(mappedBy = "medicationCheckIn")
    private Collection<PatientMedicationTaken> patientMedicationsTaken;
    @ManyToOne
    private Patient patient;

    public CheckIn() {
    }

    public CheckIn(List<PatientAnswer> patientAnswers, Date when) {
        this.patientAnswers = patientAnswers;
        this.when = when;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public Collection<PatientAnswer> getPatientAnswers() {
        return patientAnswers;
    }

    public void setPatientAnswers(Collection<PatientAnswer> patientAnswers) {
        this.patientAnswers = patientAnswers;
    }

    public Collection<PatientMedicationTaken> getPatientMedicationsTaken() {
        return patientMedicationsTaken;
    }

    public void setPatientMedicationsTaken(Collection<PatientMedicationTaken> patientMedicationsTaken) {
        this.patientMedicationsTaken = patientMedicationsTaken;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
