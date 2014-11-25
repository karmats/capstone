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
        patients.add(new Patient("janedoe", 100, "Jane", "Doe", new Date(), doctor));
        List<PainMedication> pm = new ArrayList<>();
        pm.add(meds.get(0));
        patients.get(0).setPainMedications(pm);
        patients.add(new Patient("johndoe", 101, "John", "Doe", new Date(), doctor));
        return patients;
    }

    public static List<Question> createQuestions() {
        List<Question> questions = new ArrayList<Question>();
        questions.add(new Question("How bad do you want it?"));
        questions.add(new Question("Are you sure?"));
        return questions;
    }

    public static List<Answer> createAnswers(List<Question> questions) {
        List<Answer> answers = new ArrayList<Answer>();
        answers.add(new Answer("Pretty bad", 1, "w00t! pretty bad?", questions.get(0)));
        answers.add(new Answer("Not very", 0, null, questions.get(0)));
        answers.add(new Answer("Yes", 1, "omg! really sure :(", questions.get(1)));
        answers.add(new Answer("No", 0, null, questions.get(1)));
        answers.add(new Answer("Pretty", 0, null, questions.get(1)));
        return answers;
    }

    public static List<PainMedication> createPainMedications() {
        List<PainMedication> medications = new ArrayList<PainMedication>();
        medications.add(new PainMedication("abc123", "Treo"));
        medications.add(new PainMedication("def456", "Lortab"));
        medications.add(new PainMedication("ghi789", "OxyContin"));
        return medications;
    }
}
