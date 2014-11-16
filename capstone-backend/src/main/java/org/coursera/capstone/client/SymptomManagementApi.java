package org.coursera.capstone.client;

import java.util.Collection;

import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.entity.Question;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit annotations so that clients can automatically convert the
 * 
 * 
 * @author mats
 *
 */
public interface SymptomManagementApi {

    // Used in search for patients
    public static final String NAME_PARAMETER = "name";
    // Used in findByUsername for doctor and patient repository
    public static final String USERNAME_PARAMETER = "username";

    public static final String TOKEN_PATH = "/oauth/token";

    // The path where we expect the patient service to live
    public static final String PATIENT_SVC_PATH = "/patient";
    // The path where we expect the patient data service to live
    public static final String PATIENT_INFO_SVC_PATH = PATIENT_SVC_PATH + "/{username}";
    // The path where we expect the doctor service to live
    public static final String DOCTOR_SVC_PATH = "/doctor";
    // The path where we expect the doctor data service to live
    public static final String DOCTOR_INFO_SVC_PATH = DOCTOR_SVC_PATH + "/{username}";
    // The path where we expec the doctor patients to live
    public static final String DOCTOR_PATIENTS_SVC_PATH = DOCTOR_INFO_SVC_PATH + "/patient";
    // The path where we expect the question service to live
    public static final String QUESTION_SVC_PATH = "/question";
    // The path where we expect the user info service to live
    public static final String USER_SVC_PATH = "/user";

    // The path to search videos by title
    public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";

    @GET(PATIENT_SVC_PATH)
    public Collection<Patient> getPatientList();

    @GET(DOCTOR_SVC_PATH)
    public Collection<Doctor> getDoctorList();

    @GET(QUESTION_SVC_PATH)
    public Collection<Question> getQuestionList();

    @POST(PATIENT_SVC_PATH)
    public Void addPatient(@Body Patient v);

    @GET(PATIENT_NAME_SEARCH_PATH)
    public Collection<Patient> findByName(@Query(NAME_PARAMETER) String name);

}
