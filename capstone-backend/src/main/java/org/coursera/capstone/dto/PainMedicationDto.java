package org.coursera.capstone.dto;

import org.coursera.capstone.entity.PainMedication;

public class PainMedicationDto {

    private String name;
    private String medicationId;

    public PainMedicationDto() {
    }

    public PainMedicationDto(String name, String medicationId) {
        this.name = name;
        this.medicationId = medicationId;
    }

    public PainMedicationDto(PainMedication painMedication) {
        this.medicationId = painMedication.getMedicationId();
        this.name = painMedication.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
}
