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
 * @author jules
 *
 */
public interface SymptomManagementApi {

    public static final String NAME_PARAMETER = "name";

    public static final String TOKEN_PATH = "/oauth/token";

    // The path where we expect the patient service to live
    public static final String PATIENT_SVC_PATH = "/patient";
    // The path where we expect the doctor service to live
    public static final String DOCTOR_SVC_PATH = "/doctor";
    // The path where we expect the question service to live
    public static final String QUESTION_SVC_PATH = "/question";

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
