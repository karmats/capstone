package org.coursera.capstone.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * A simple object to represent a Patients pain medication
 */
@Entity
public class PatientPainMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private Patient patient;

    @OneToOne
    private PainMedication painMedication;

    public PatientPainMedication() {
    }

    public PatientPainMedication(Patient patient, PainMedication painMedication) {
        this.patient = patient;
        this.painMedication = painMedication;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
