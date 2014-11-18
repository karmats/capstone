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

    private Date when;

    @ManyToOne
    private PainMedication medication;
    @ManyToOne
    private CheckIn medicationCheckIn;

    public PatientMedicationTaken() {
    }

    public PainMedication getMedication() {
        return medication;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
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
