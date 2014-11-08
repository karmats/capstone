package org.coursera.capstone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.Patient;

/**
 * Class that creates initial test data when the application starts up
 * 
 * @author matros
 *
 */
public class InitalTestData {

    // Instance of this class shouldn't be made
    private InitalTestData() {
    }

    public static List<Patient> createTestPatients(Doctor doctor) {
        List<Patient> patients = new ArrayList<Patient>();
        patients.add(new Patient("matros", 100, "Mats", "Roshauw", new Date(), doctor));
        patients.add(new Patient("lillan", 101, "Martine", "Roshauw", new Date(), doctor));
        return patients;
    }
}
