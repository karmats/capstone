package org.coursera.capstone.android.http.api;

import org.coursera.capstone.android.parcelable.CheckInRequest;
import org.coursera.capstone.android.parcelable.CheckInResponse;
import org.coursera.capstone.android.parcelable.Doctor;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;
import org.coursera.capstone.android.parcelable.User;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Api to symptom management services.
 */
public interface SymptomManagementApi {

    public static final String NAME_PARAMETER = "name";
    public static final String USERNAME_PARAMETER = "username";

    // Service host
    public static final String HOST = "https://192.168.1.110:8443";

    // Login service oauth2
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
    public static final String USER_INFO_SVC_PATH = "/user";
    // The path where we expect the check in service to live
    public static final String CHECK_IN_SVC_PATH = "/checkin";
    // The path where we expect the check in patient service to live
    public static final String CHECK_IN_PATIENT_SVC_PATH = "/checkin" + "/{username}";
    // The path where we expect the pain medication service to live
    public static final String PAIN_MEDICATION_SVC_PATH = "/medication";
    // The path where we expect the update pain medication service to live
    public static final String PAIN_MEDICATION_UPDATE_SVC_PATH = PAIN_MEDICATION_SVC_PATH + "/{username}";

    // The path to search patients by name
    public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";

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

    @GET(DOCTOR_INFO_SVC_PATH)
    Doctor getDoctorInformation(@Path(USERNAME_PARAMETER) String username);

    @GET(DOCTOR_PATIENTS_SVC_PATH)
    List<Patient> getDoctorPatients(@Path(USERNAME_PARAMETER) String username);

    @GET(QUESTION_SVC_PATH)
    List<Question> getQuestions();

    @POST(CHECK_IN_SVC_PATH)
    Response checkIn(@Body CheckInRequest checkInRequest);

    @GET(PAIN_MEDICATION_SVC_PATH)
    List<PainMedication> getPainMedications();

    @PUT(PAIN_MEDICATION_UPDATE_SVC_PATH)
    Response updatePainMedications(@Path(USERNAME_PARAMETER) String username,
                                   @Body List<PainMedication> updatedPainMedications);

    @GET(CHECK_IN_PATIENT_SVC_PATH)
    public List<CheckInResponse> getPatientCheckIn(@Path(USERNAME_PARAMETER) String username);

}