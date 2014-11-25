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
    // Since a check in can have more than one alert but is stored as a String
    // we have a delimiter for seperating them
    public static final String AlERTS_DELIMITER = ";";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date checkInTime;
    // If an alert is present for this check-in
    private String alert;

    @OneToMany(mappedBy = "answerCheckIn")
    private Collection<PatientAnswer> patientAnswers;
    @OneToMany(mappedBy = "medicationCheckIn")
    private Collection<PatientMedicationTaken> patientMedicationsTaken;
    @ManyToOne
    private Patient patient;

    public CheckIn() {
    }

    public CheckIn(Patient patient, List<PatientAnswer> patientAnswers, List<PatientMedicationTaken> medicationsTaken,
            Date checkInTime) {
        this.patient = patient;
        this.patientAnswers = patientAnswers;
        this.patientMedicationsTaken = medicationsTaken;
        this.checkInTime = checkInTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
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
