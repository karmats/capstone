package org.coursera.capstone.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Class to include at a check-in. If a patient took his/hers pain medications and when
 * 
 * @author matros
 *
 */
@Entity
public class PatientMedicationTaken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date medicationTime;

    @ManyToOne
    private PainMedication medication;
    @ManyToOne
    private CheckIn medicationCheckIn;

    public PatientMedicationTaken() {
    }

    public PatientMedicationTaken(Date medicationTime, PainMedication medication) {
        this.medicationTime = medicationTime;
        this.medication = medication;
    }

    public PainMedication getMedication() {
        return medication;
    }

    public Date getMedicationTime() {
        return medicationTime;
    }

    public void setMedicationTime(Date medicationTime) {
        this.medicationTime = medicationTime;
    }

    public void setMedication(PainMedication medication) {
        this.medication = medication;
    }

    public CheckIn getMedicationCheckIn() {
        return medicationCheckIn;
    }

    public void setMedicationCheckIn(CheckIn medicationCheckIn) {
        this.medicationCheckIn = medicationCheckIn;
    }

}
