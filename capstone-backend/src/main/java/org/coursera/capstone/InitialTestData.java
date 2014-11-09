package org.coursera.capstone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.entity.Answer;
import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.entity.Question;

/**
 * Class that creates initial test data when the application starts up
 * 
 * @author matros
 *
 */
public class InitialTestData {

    // Instance of this class shouldn't be made
    private InitialTestData() {
    }

    /**
     * Creates a list of patients
     * 
     * @param doctor
     *            The patient doctor
     * @return {@link List}
     */
    public static List<Patient> createTestPatients(Doctor doctor, List<PainMedication> meds) {
        List<Patient> patients = new ArrayList<Patient>();
        patients.add(new Patient("matros", 100, "Mats", "Roshauw", new Date(), doctor));
        patients.get(0).setPainMedications(meds);
        patients.add(new Patient("lillan", 101, "Martine", "Roshauw", new Date(), doctor));
        return patients;
    }

    public static List<Question> createQuestions() {
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        Question q = new Question("How bad do you want it?");
        answers.add(new Answer("Pretty bad", q));
        answers.add(new Answer("Not very", q));
        q.setAnswers(answers);
        questions.add(q);
        return questions;
    }

    public static List<PainMedication> createPainMedications() {
        List<PainMedication> medications = new ArrayList<PainMedication>();
        medications.add(new PainMedication("abc123", "Treo"));
        return medications;
    }
}
