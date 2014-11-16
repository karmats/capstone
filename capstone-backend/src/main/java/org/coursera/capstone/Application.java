package org.coursera.capstone;

import java.util.List;

import org.coursera.capstone.auth.OAuth2SecurityConfiguration;
import org.coursera.capstone.entity.Answer;
import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.entity.Question;
import org.coursera.capstone.json.ResourcesMapper;
import org.coursera.capstone.repository.AnswerRepository;
import org.coursera.capstone.repository.DoctorRepository;
import org.coursera.capstone.repository.PainMedicationRepository;
import org.coursera.capstone.repository.PatientRepository;
import org.coursera.capstone.repository.QuestionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

//Tell Spring to automatically inject any dependencies that are marked in
//our classes with @Autowired
@EnableAutoConfiguration
// Tell Spring that this object represents a Configuration for the
// application
@Configuration
// Tell Spring to go and scan our controller package (and all sub packages) to
// find any Controllers or other components that are part of our applciation.
// Any class in this package that is annotated with @Controller is going to be
// automatically discovered and connected to the DispatcherServlet.
@ComponentScan
// We use the @Import annotation to include our OAuth2SecurityConfiguration
// as part of this configuration so that we can have security and oauth
// setup by Spring
@Import(OAuth2SecurityConfiguration.class)
public class Application extends RepositoryRestMvcConfiguration {

    // The app now requires that you pass the location of the keystore and
    // the password for your private key that you would like to setup HTTPS
    // with. In Eclipse, you can set these options by going to:
    // 1. Run->Run Configurations
    // 2. Under Java Applications, select your run configuration for this app
    // 3. Open the Arguments tab
    // 4. In VM Arguments, provide the following information to use the
    // default keystore provided with the sample code:
    //
    // -Dkeystore.file=src/main/resources/private/keystore
    // -Dkeystore.pass=changeit
    //
    // 5. Note, this keystore is highly insecure! If you want more securtiy, you
    // should obtain a real SSL certificate:
    //
    // http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
    //
    // Tell Spring to launch our app!
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        // Create some initial data
        DoctorRepository doctorRepo = ctx.getBean(DoctorRepository.class);
        PatientRepository patientRepo = ctx.getBean(PatientRepository.class);
        PainMedicationRepository medicationRep = ctx.getBean(PainMedicationRepository.class);
        QuestionRepository questionRepo = ctx.getBean(QuestionRepository.class);
        AnswerRepository answerRepo = ctx.getBean(AnswerRepository.class);
        // Pain medications
        List<PainMedication> medications = InitialTestData.createPainMedications();
        medicationRep.save(medications);
        // Doctors
        Doctor d = new Doctor("drporter", "Doctor", "Porter");
        doctorRepo.save(d);
        // Patients
        List<Patient> patients = InitialTestData.createTestPatients(d, medications);
        patients = (List<Patient>) patientRepo.save(patients);
        // Questions
        List<Question> questions = InitialTestData.createQuestions();
        questionRepo.save(questions);
        // Answers
        answerRepo.save(InitialTestData.createAnswers(questions));

    }

    // We are overriding the bean that RepositoryRestMvcConfiguration
    // is using to convert our objects into JSON so that we can control
    // the format. The Spring dependency injection will inject our instance
    // of ObjectMapper in all of the spring data rest classes that rely
    // on the ObjectMapper. This is an example of how Spring dependency
    // injection allows us to easily configure dependencies in code that
    // we don't have easy control over otherwise.
    @Override
    public ObjectMapper halObjectMapper() {
        return new ResourcesMapper();
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        // We need to expose the ids for the answer and question class to know what question and what answer the patient
        // answers
        config.exposeIdsFor(Answer.class, Question.class);
        super.configureRepositoryRestConfiguration(config);
    }

}
