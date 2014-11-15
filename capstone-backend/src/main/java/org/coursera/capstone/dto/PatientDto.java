package org.coursera.capstone.dto;

import java.util.Date;

import org.coursera.capstone.auth.User;
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

    public PatientDto(String username, String firstName, String lastName, DoctorDto doctor) {
        super(username, firstName, lastName, User.UserAuthority.PATIENT.getRole());
        this.doctor = doctor;
    }

    public PatientDto(Patient p) {
        super(p.getUsername(), p.getFirstName(), p.getLastName(), User.UserAuthority.PATIENT.getRole());
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
}
