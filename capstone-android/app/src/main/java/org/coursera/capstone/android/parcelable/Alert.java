package org.coursera.capstone.android.parcelable;

import java.util.List;

/**
 * Created by matros on 30/11/14.
 */
public class Alert {

    private String firstName;
    private String lastName;
    private List<String> alerts;

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
