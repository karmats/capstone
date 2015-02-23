package org.coursera.capstone;

import java.util.Collection;
import java.util.List;

import org.coursera.capstone.auth.OAuth2SecurityConfiguration;
import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.entity.Question;
import org.coursera.capstone.repository.AnswerRepository;
import org.coursera.capstone.repository.DoctorRepository;
import org.coursera.capstone.repository.PainMedicationRepository;
import org.coursera.capstone.repository.PatientRepository;
import org.coursera.capstone.repository.QuestionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
@Import({OAuth2SecurityConfiguration.class, RestConfig.class})
public class Application extends SpringBootServletInitializer {

    private static Class<Application> applicationClass = Application.class;

    // Tell Spring to launch our app!
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(applicationClass, args);

        // Create some initial data
        DoctorRepository doctorRepo = ctx.getBean(DoctorRepository.class);
        PatientRepository patientRepo = ctx.getBean(PatientRepository.class);
        PainMedicationRepository medicationRep = ctx.getBean(PainMedicationRepository.class);
        QuestionRepository questionRepo = ctx.getBean(QuestionRepository.class);
        AnswerRepository answerRepo = ctx.getBean(AnswerRepository.class);
        // Pain medications
        List<PainMedication> medications = InitialTestData.createPainMedications();
        medicationRep.save(medications);
        // Doctors and patients
        List<Doctor> doctors = InitialTestData.createTestDoctorAndPatients(medications);
        for (Doctor d : doctors) {
            doctorRepo.save(d);
            Collection<Patient> patients = d.getPatients();
            patientRepo.save(patients);
        }

        // Questions and answers
        List<Question> questions = InitialTestData.createQuestions();
        for (Question q : questions) {
            questionRepo.save(q);
            answerRepo.save(q.getAnswers());
        }

    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

}
