package org.coursera.capstone.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jdk.nashorn.internal.ir.annotations.Ignore;

import org.coursera.capstone.TestData;
import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.AlertDto;
import org.coursera.capstone.dto.CheckInPatientResponseDto;
import org.coursera.capstone.dto.CheckInRequestDto;
import org.coursera.capstone.dto.PatientDto;
import org.junit.Test;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Response;

public class SymptomManagementClientApiTest {

    private final String USERNAME_DOCTOR = "hibe";
    private final String PASSWORD_DOCTOR = "pass";
    private final String CLIENT_ID = "mobile";
    private final String USERNAME_PATIENT = "jeff";
    private final String PASSWORD_PATIENT = "pass";
    private final Long PATIENT_MEDICAL_RECORD_NO = 202L;

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
            patientService.getPatientList(USERNAME_DOCTOR);
            fail("Server should denied patient from accessing doctor api");
        } catch (RetrofitError e) {
            // Forbidden
            assertTrue(e.getResponse().getStatus() == 403);
        }
    }

    @Test
    public void onlyPatientsShouldBeAbleToCheckIn() throws Exception {
        try {
            doctorService.checkIn(new CheckInRequestDto());
            fail("Server should denied doctor from performing a check-in");
        } catch (RetrofitError e) {
            // Forbidden
            assertTrue(e.getResponse().getStatus() == 403);
        }
    }

    @Test
    @Ignore
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
        // Submit an "alert check-in" six hours ago
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -16);
        CheckInRequestDto firstCheckInRequest = TestData.createCheckInRequest(patientService,
                PATIENT_MEDICAL_RECORD_NO, c.getTime());
        patientService.checkIn(firstCheckInRequest);

        // Submit another today
        CheckInRequestDto secondCheckInRequest = TestData.createCheckInRequest(patientService,
                PATIENT_MEDICAL_RECORD_NO, new Date());
        patientService.checkIn(secondCheckInRequest);
        AlertDto alerts = doctorService.getPatientAlerts(USERNAME_PATIENT);
        // Should have an alert
        assertTrue(alerts.getAlerts().size() > 0);
    }

    @Test
    public void testSearchByName() throws Exception {
        Collection<PatientDto> patients = doctorService.findByName("name");
        assertNotNull(patients);
    }

}
