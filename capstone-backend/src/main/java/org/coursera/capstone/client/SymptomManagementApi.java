package org.coursera.capstone.client;

import java.util.List;

import org.coursera.capstone.dto.CheckInPatientResponseDto;
import org.coursera.capstone.dto.CheckInRequestDto;
import org.coursera.capstone.dto.PatientDto;
import org.coursera.capstone.entity.Question;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
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
    // The path where we expect the check in service to live
    public static final String CHECK_IN_SVC_PATH = "/checkin";
    // The path where we expect the check in patient service to live
    public static final String CHECK_IN_PATIENT_SVC_PATH = CHECK_IN_SVC_PATH + "/{username}";
    // The path where we expect the check in patient alerts service to live
    public static final String CHECK_IN_PATIENT_ALERTS_SVC_PATH = CHECK_IN_PATIENT_SVC_PATH + "/alerts";
    // The path where we expect the pain medication service to live
    public static final String PAIN_MEDICATION_SVC_PATH = "/medication";
    // The path where we expect the update pain medication service to live
    public static final String PAIN_MEDICATION_UPDATE_SVC_PATH = PAIN_MEDICATION_SVC_PATH + "/{username}";

    // The path to search videos by title
    public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH + "/search/findByName";

    @GET(DOCTOR_PATIENTS_SVC_PATH)
    public List<PatientDto> getPatientList(@Path(USERNAME_PARAMETER) String username);

    @GET(PATIENT_NAME_SEARCH_PATH)
    public List<PatientDto> findByName(@Query(NAME_PARAMETER) String name);

    @GET(QUESTION_SVC_PATH)
    public List<Question> getQuestions();

    @POST(CHECK_IN_SVC_PATH)
    public Response checkIn(@Body CheckInRequestDto checkIn);

    @GET(CHECK_IN_PATIENT_SVC_PATH)
    public List<CheckInPatientResponseDto> getPatientCheckIn(@Path(USERNAME_PARAMETER) String username);

    @GET(CHECK_IN_PATIENT_ALERTS_SVC_PATH)
    public List<String> getPatientAlerts(@Path(USERNAME_PARAMETER) String username);

}
