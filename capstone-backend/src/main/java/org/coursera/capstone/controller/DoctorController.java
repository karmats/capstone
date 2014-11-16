package org.coursera.capstone.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.DoctorDto;
import org.coursera.capstone.dto.PatientDto;
import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to handle all doctor calls
 * 
 * @author matros
 *
 */
@Controller
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepo;

    @RequestMapping(value = SymptomManagementApi.DOCTOR_INFO_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody DoctorDto getDoctorInfo(@PathVariable(SymptomManagementApi.USERNAME_PARAMETER) String username) {
        Doctor d = doctorRepo.findByUsername(username);
        return new DoctorDto(d);
    }

    @RequestMapping(value = SymptomManagementApi.DOCTOR_PATIENTS_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<PatientDto> getDoctorPatients(
            @PathVariable(SymptomManagementApi.USERNAME_PARAMETER) String username) {
        Collection<PatientDto> result = new ArrayList<PatientDto>();
        Doctor d = doctorRepo.findByUsername(username);
        for (Patient p : d.getPatients()) {
            result.add(new PatientDto(p));
        }
        return result;
    }
}
