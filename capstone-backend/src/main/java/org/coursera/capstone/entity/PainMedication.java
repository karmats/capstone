package org.coursera.capstone.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PainMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String medicationId;
    private String name;

    @JsonIgnore
    @ManyToMany
    private Collection<Patient> patients;
    @JsonIgnore
    @OneToMany(mappedBy = "medication")
    private Collection<PatientMedicationTaken> patientMedicationsTaken;

    public PainMedication() {
    }

    public PainMedication(String medicationId, String name) {
        this.medicationId = medicationId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Collection<Patient> patients) {
        this.patients = patients;
    }
}
