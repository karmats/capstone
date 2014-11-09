package org.coursera.capstone.android.http.api;

import org.coursera.capstone.android.parceable.Doctor;
import org.coursera.capstone.android.parceable.Patient;

import java.util.Collection;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Api to symptom management services.
 */
public interface SymptomManagementApi {

    public static final String NAME_PARAMETER = "name";

     // Service host
    public static final String HOST = "https://192.168.1.76:8443";

     // Login service oauth2
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
    public List<Patient> getPatientList();

    @GET(DOCTOR_SVC_PATH)
    public List<Doctor> getDoctorList();

    @POST(PATIENT_SVC_PATH)
    public Void addPatient(@Body Patient v);

    @GET(PATIENT_NAME_SEARCH_PATH)
    public Collection<Patient> findByName(@Query(NAME_PARAMETER) String name);

}
