package org.coursera.capstone.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PainMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String medicationId;
    private String name;

    public PainMedication() {
    }

    public PainMedication(String medicationId, String name) {
        this.setMedicationId(medicationId);
        this.setName(name);
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
}
