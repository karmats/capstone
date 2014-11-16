package org.coursera.capstone.controller;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.DoctorDto;
import org.coursera.capstone.dto.PatientDto;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to handle all patient calls
 * 
 * @author matros
 *
 */
@Controller
public class PatientController {

    @Autowired
    PatientRepository patientRepo;

    @RequestMapping(value = SymptomManagementApi.PATIENT_INFO_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody PatientDto getPatientInfo(
            @PathVariable(SymptomManagementApi.USERNAME_PARAMETER) String username) {
        Patient p = patientRepo.findByUsername(username);
        PatientDto result = new PatientDto(p);
        result.setBirthDate(p.getBirthDate());
        result.setMedicalRecordNumber(p.getMedicalRecordNumber());
        result.setDoctor(new DoctorDto(p.getDoctor()));
        return result;
    }
}
