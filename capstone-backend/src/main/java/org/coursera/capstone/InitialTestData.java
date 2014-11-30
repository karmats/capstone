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
    public static List<Doctor> createTestDoctorAndPatients(List<PainMedication> meds) {
        List<Doctor> result = new ArrayList<>();
        // Family guy
        Doctor elmer = new Doctor("elme", "Elmer", "Hartman");
        List<Patient> familyGuyPatients = new ArrayList<Patient>();

        Patient pete = new Patient("pete", 100, "Peter", "Griffin", new Date(), elmer);
        pete.setPainMedications(meds.subList(0, 1));
        familyGuyPatients.add(pete);

        Patient john = new Patient("john", 101, "John", "Herbert", new Date(), elmer);
        john.setPainMedications(meds.subList(1, 2));
        familyGuyPatients.add(john);

        Patient cart = new Patient("cart", 102, "Carter", "Pewterschmidt", new Date(), elmer);
        cart.setPainMedications(meds.subList(0, 2));
        familyGuyPatients.add(cart);

        elmer.setPatients(familyGuyPatients);
        result.add(elmer);

        // Simpson
        Doctor hibe = new Doctor("hibe", "Julius", "Hibbert");
        List<Patient> simpsonPatients = new ArrayList<>();

        Patient home = new Patient("home", 200, "Homer", "Simpson", new Date(), hibe);
        home.setPainMedications(meds.subList(1, 2));
        simpsonPatients.add(home);

        Patient krus = new Patient("krus", 201, "Krusty", "Krustofski", new Date(), hibe);
        krus.setPainMedications(meds.subList(0, 3));
        simpsonPatients.add(krus);

        Patient jeff = new Patient("jeff", 202, "Jeff ", "Albertson", new Date(), hibe);
        jeff.setPainMedications(meds.subList(0, 2));
        simpsonPatients.add(jeff);

        hibe.setPatients(simpsonPatients);
        result.add(hibe);
        return result;
    }

    public static List<Question> createQuestions() {
        List<Question> questions = new ArrayList<Question>();

        // Pain
        Question pain = new Question("How bad is your mouth pain/sore throat?");
        List<Answer> painAnswers = new ArrayList<>();
        painAnswers.add(new Answer("Well-controlled", 0, null, pain));
        painAnswers.add(new Answer("Moderate", 16, "Experience moderate to severe pain for 16 hours", pain));
        painAnswers.add(new Answer("Severe", 12, "Experience severe pain for 12 hours", pain));
        pain.setAnswers(painAnswers);
        questions.add(pain);

        // Eating/drinking
        Question eatDrink = new Question("Does your pain stop you from eating/drinking?");
        List<Answer> eatDrinkAnswers = new ArrayList<>();
        eatDrinkAnswers.add(new Answer("No", 0, null, eatDrink));
        eatDrinkAnswers.add(new Answer("Some", 0, null, eatDrink));
        eatDrinkAnswers.add(new Answer("I can't eat", 12, "Haven't been able to eat or drink for 12 hours", eatDrink));
        eatDrink.setAnswers(eatDrinkAnswers);
        questions.add(eatDrink);

        return questions;
    }

    public static List<PainMedication> createPainMedications() {
        List<PainMedication> medications = new ArrayList<PainMedication>();
        medications.add(new PainMedication("abc123", "Hydrocodone"));
        medications.add(new PainMedication("def456", "Lortab"));
        medications.add(new PainMedication("ghi789", "OxyContin"));
        return medications;
    }
}
