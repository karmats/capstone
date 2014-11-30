package org.coursera.capstone.dto;

import java.util.List;

import org.coursera.capstone.entity.Patient;

public class AlertDto {

    private String firstName;
    private String lastName;
    private List<String> alerts;

    public AlertDto(Patient p, List<String> alerts) {
        this.setFirstName(p.getFirstName());
        this.setLastName(p.getLastName());
        this.setAlerts(alerts);
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

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }
}
