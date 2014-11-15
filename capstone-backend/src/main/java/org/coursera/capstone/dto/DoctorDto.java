package org.coursera.capstone.dto;

import org.coursera.capstone.auth.User;
import org.coursera.capstone.entity.Doctor;

/**
 * Doctor response class. 
 * 
 * @author matros
 *
 */
public class DoctorDto extends UserInfoDto {

    public DoctorDto(String username, String firstName, String lastName) {
        super(username, firstName, lastName, User.UserAuthority.DOCTOR.getRole());
    }
    
    public DoctorDto(Doctor d) {
        super(d.getUsername(), d.getFirstName(), d.getLastName(), User.UserAuthority.DOCTOR.getRole());
    }
}
