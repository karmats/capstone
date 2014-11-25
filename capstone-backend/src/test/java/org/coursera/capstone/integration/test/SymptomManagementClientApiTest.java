package org.coursera.capstone.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.TestData;
import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SecuredRestException;
import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.CheckInPatientResponseDto;
import org.coursera.capstone.dto.CheckInRequestDto;
import org.coursera.capstone.dto.PatientDto;
import org.junit.Test;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Response;

/**
 * 
 * This integration test sends a POST request to the VideoServlet to add a new video and then sends a second GET request
 * to check that the video showed up in the list of videos. Actual network communication using HTTP is performed with
 * this test.
 * 
 * The test requires that the VideoSvc be running first (see the directions in the README.md file for how to launch the
 * Application).
 * 
 * To run this test, right-click on it in Eclipse and select "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just directly makes method calls on a
 * VideoSvc object are essentially identical. All that changes is the setup of the videoService variable. Yes, this
 * could be refactored to eliminate code duplication...but the goal was to show how much Retrofit simplifies interaction
 * with our service!
 * 
 * @author jules
 *
 */
public class SymptomManagementClientApiTest {

    private final String USERNAME_DOCTOR = "drporter";
    private final String PASSWORD_DOCTOR = "pass";
    private final String CLIENT_ID = "mobile";
    private final String USERNAME_PATIENT = "janedoe";
    private final String PASSWORD_PATIENT = "pass";
    private final Long PATIENT_MEDICAL_RECORD_NO = 100L;

    private final String TEST_URL = "https://localhost:8443";

    private SymptomManagementApi doctorService = new SecuredRestBuilder()
            .setLoginEndpoint(TEST_URL + SymptomManagementApi.TOKEN_PATH).setUsername(USERNAME_DOCTOR)
            .setPassword(PASSWORD_DOCTOR).setClientId(CLIENT_ID)
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient())).setEndpoint(TEST_URL)
            .setLogLevel(LogLevel.FULL).build().create(SymptomManagementApi.class);

    private SymptomManagementApi patientService = new SecuredRestBuilder()
            .setLoginEndpoint(TEST_URL + SymptomManagementApi.TOKEN_PATH).setUsername(USERNAME_PATIENT)
            .setPassword(PASSWORD_PATIENT).setClientId(CLIENT_ID)
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient())).setEndpoint(TEST_URL)
            .setLogLevel(LogLevel.FULL).build().create(SymptomManagementApi.class);

    @Test
    public void onlyDoctorsShouldHaveAccessToDoctorApi() throws Exception {
        // Should be fine
        doctorService.getPatientList(USERNAME_DOCTOR);
        // Should fail
        try {
            patientService.getPatientList(USERNAME_PATIENT);
            fail("Server should denied patient from accessing doctor api");
        } catch (RetrofitError e) {
            assert (e.getCause() instanceof SecuredRestException);
        }
    }

    @Test
    public void testCheckInFlow() throws Exception {
        CheckInRequestDto checkInRequest = TestData.createCheckInRequest(patientService, PATIENT_MEDICAL_RECORD_NO,
                new Date());
        // Patient submits a check-in
        Response response = patientService.checkIn(checkInRequest);
        assertEquals(200, response.getStatus());

        // Get the created request, need the doctor service for this
        List<CheckInPatientResponseDto> checkInResponse = doctorService.getPatientCheckIn(USERNAME_PATIENT);
        assertTrue(checkInResponse.size() > 0);
    }

    @Test
    public void shouldBeAlerted() throws Exception {
        // Submit an "alert check-in" last day
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -6);
        CheckInRequestDto firstCheckInRequest = TestData.createCheckInRequest(patientService,
                PATIENT_MEDICAL_RECORD_NO, c.getTime());
        patientService.checkIn(firstCheckInRequest);
        List<CheckInPatientResponseDto> checkInResponse = doctorService.getPatientCheckIn(USERNAME_PATIENT);

        // Submit another today
        CheckInRequestDto secondCheckInRequest = TestData.createCheckInRequest(patientService,
                PATIENT_MEDICAL_RECORD_NO, new Date());
        patientService.checkIn(secondCheckInRequest);
        checkInResponse = doctorService.getPatientCheckIn(USERNAME_PATIENT);
        // Should have an alert
        assertTrue(checkInResponse.get(0).getAlerts().size() > 0);
    }

    @Test
    public void testSearchByName() throws Exception {
        Collection<PatientDto> patients = doctorService.findByName("name");
        assertNotNull(patients);
    }

}
