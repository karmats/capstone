package org.coursera.capstone.controller;

import java.security.Principal;

import org.coursera.capstone.auth.User.UserAuthority;
import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.DoctorDto;
import org.coursera.capstone.dto.PatientDto;
import org.coursera.capstone.dto.UserDto;
import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.repository.DoctorRepository;
import org.coursera.capstone.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller used to fetch information about the logged in user.
 * 
 * @author mats
 *
 */
@Controller
public class UserInfoController {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    DoctorRepository doctorRepo;
    @Autowired
    PatientRepository patientRepo;

    @RequestMapping(value = SymptomManagementApi.USER_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody UserDto getUserInfo(Principal p) {
        String username = p.getName();
        LOG.info("Getting info for user "+ username);
        // Check if the user exists in patient db
        Patient patient = patientRepo.findByUsername(username);
        if (patient != null) {
            return new UserDto(patient.getUsername(), patient.getFirstName(), patient.getLastName(),
                    UserAuthority.PATIENT.getRole());
        } else {
            // This is a doctor
            Doctor doctor = doctorRepo.findByUsername(username);
            return new UserDto(doctor.getUsername(), doctor.getFirstName(), doctor.getLastName(),
                    UserAuthority.DOCTOR.getRole());
        }
    }

    @RequestMapping(value = SymptomManagementApi.USER_SVC_PATH + "/patient", method = RequestMethod.GET)
    public @ResponseBody PatientDto getPatient(@RequestParam("username") String username) {
        Patient p = patientRepo.findByUsername(username);
        Doctor d = p.getDoctor();
        PatientDto result = new PatientDto(p);
        result.setDoctor(new DoctorDto(d));
        return result;
    }
}
