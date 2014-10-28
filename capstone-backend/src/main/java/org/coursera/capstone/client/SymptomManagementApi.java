package org.coursera.capstone.client;

import java.util.Collection;

import org.coursera.capstone.entity.Patient;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The interface is used to
 * provide a contract for client/server interactions. The interface is annotated
 * with Retrofit annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface SymptomManagementApi {

    public static final String PASSWORD_PARAMETER = "password";

    public static final String USERNAME_PARAMETER = "username";

    public static final String NAME_PARAMETER = "name";

    public static final String TOKEN_PATH = "/oauth/token";

    // The path where we expect the patient service to live
    public static final String PATIENT_SVC_PATH = "/patient";

    // The path to search videos by title
    public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";

    @GET(PATIENT_SVC_PATH)
    public Collection<Patient> getPatientList();

    @POST(PATIENT_SVC_PATH)
    public Void addPatient(@Body Patient v);

    @GET(PATIENT_NAME_SEARCH_PATH)
    public Collection<Patient> findByName(@Query(NAME_PARAMETER) String title);

}
