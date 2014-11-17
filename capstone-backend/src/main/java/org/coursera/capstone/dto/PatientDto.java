package org.coursera.capstone.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.auth.User;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.entity.Patient;

/**
 * Patient result class
 * 
 * @author matros
 *
 */
public class PatientDto extends UserInfoDto {

    private long medicalRecordNumber;
    private Date birthDate;

    private DoctorDto doctor;
    private List<PainMedicationDto> medications;

    public PatientDto(String username, String firstName, String lastName, DoctorDto doctor) {
        super(username, firstName, lastName, User.UserAuthority.PATIENT.getRole());
        this.doctor = doctor;
    }

    public PatientDto(Patient p) {
        super(p.getUsername(), p.getFirstName(), p.getLastName(), User.UserAuthority.PATIENT.getRole());
        this.medications = new ArrayList<PainMedicationDto>();
        for (PainMedication pm : p.getPainMedications()) {
            this.medications.add(new PainMedicationDto(pm));
        }
    }

    public long getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(long medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDto doctor) {
        this.doctor = doctor;
    }

    public List<PainMedicationDto> getMedications() {
        return medications;
    }

    public void setMedications(List<PainMedicationDto> medications) {
        this.medications = medications;
    }
}
