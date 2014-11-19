package org.coursera.capstone.dto;


/**
 * Describes a simple user information object that are fetched in /user. Both Doctor and Patient entity extend this
 * 
 * @author mats
 *
 */
public class UserDto {

    private String username;
    private String firstName;
    private String lastName;
    // User must either be doctor or patient, can't be both
    private String role;
    
    public UserDto(String username, String firstName, String lastName, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
