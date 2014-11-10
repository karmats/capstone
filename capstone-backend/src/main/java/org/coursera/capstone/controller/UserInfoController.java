package org.coursera.capstone.controller;

import java.security.Principal;

import org.coursera.capstone.auth.User;
import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.entity.UserInfo;
import org.coursera.capstone.repository.DoctorRepository;
import org.coursera.capstone.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller used to fetch information about the logged in user.
 * 
 * @author mats
 *
 */
@Controller
public class UserInfoController {

    @Autowired
    DoctorRepository doctorRepo;
    @Autowired
    PatientRepository patientRepo;

    @RequestMapping(value = SymptomManagementApi.USER_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody UserInfo getUserInfo(Principal p) {
        String username = p.getName();
        // Check if the user exists in patient db
        UserInfo userInfo = patientRepo.findByUsername(username);
        if (userInfo == null) {
            // Patient was null, this is a doctor
            userInfo = doctorRepo.findByUsername(username);
            userInfo.setRole(User.UserAuthority.DOCTOR.getRole());
        } else {
            userInfo.setRole(User.UserAuthority.PATIENT.getRole());
        }
        return userInfo;
    }
}
