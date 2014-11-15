package org.coursera.capstone.android.http.api;

import org.coursera.capstone.android.parceable.Doctor;
import org.coursera.capstone.android.parceable.Patient;
import org.coursera.capstone.android.parceable.User;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Api to symptom management services.
 */
public interface SymptomManagementApi {

    public static final String NAME_PARAMETER = "name";
    public static final String USERNAME_PARAMETER = "username";

    // Service host
    public static final String HOST = "https://192.168.1.76:8443";

    // Login service oauth2
    public static final String TOKEN_PATH = "/oauth/token";

    // The path where we expect the patient service to live
    public static final String PATIENT_SVC_PATH = "/patient";
    // The path where we expect the patient data service to live
    public static final String PATIENT_INFO_SVC_PATH = PATIENT_SVC_PATH + "/{username}";
    // The path where we expect the doctor service to live
    public static final String DOCTOR_SVC_PATH = "/doctor";
    // The path where we expect the question service to live
    public static final String QUESTION_SVC_PATH = "/question";
    // The path where we expect the user info service to live
    public static final String USER_INFO_SVC_PATH = "/user";

    // The path to search patients by name
    public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";
    // The path to get a patient by username
    public static final String PATIENT_USERNAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByUsername";
    // The path to get a doctor by username
    public static final String DOCTOR_USERNAME_SEARCH_PATH = DOCTOR_SVC_PATH + "/search/findByUsername";

    @GET(PATIENT_SVC_PATH)
    List<Patient> getPatientList();

    @GET(DOCTOR_SVC_PATH)
    List<Doctor> getDoctorList();

    @GET(USER_INFO_SVC_PATH)
    User getUserInfo();

    @GET(PATIENT_NAME_SEARCH_PATH)
    List<Patient> findByName(@Query(NAME_PARAMETER) String name);

    @GET(PATIENT_INFO_SVC_PATH)
    Patient getPatientInformation(@Path(USERNAME_PARAMETER) String username);

    @GET(DOCTOR_USERNAME_SEARCH_PATH)
    List<Doctor> findDoctorByUsername(@Query(USERNAME_PARAMETER) String username);

}
