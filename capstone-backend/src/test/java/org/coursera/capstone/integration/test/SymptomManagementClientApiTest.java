package org.coursera.capstone.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.UUID;

import org.coursera.capstone.TestData;
import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SecuredRestException;
import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.entity.Patient;
import org.junit.Test;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.google.gson.JsonObject;

/**
 * 
 * This integration test sends a POST request to the VideoServlet to add a new
 * video and then sends a second GET request to check that the video showed up
 * in the list of videos. Actual network communication using HTTP is performed
 * with this test.
 * 
 * The test requires that the VideoSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a VideoSvc object are essentially identical.
 * All that changes is the setup of the videoService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author jules
 *
 */
public class SymptomManagementClientApiTest {

    private final String USERNAME = "admin";
    private final String PASSWORD = "pass";
    private final String CLIENT_ID = "doctor_client";
    private final String READ_ONLY_CLIENT_ID = "patient_client";

    private final String TEST_URL = "https://localhost:8443";

    private SymptomManagementApi smService = new SecuredRestBuilder()
            .setLoginEndpoint(TEST_URL + SymptomManagementApi.TOKEN_PATH).setUsername(USERNAME).setPassword(PASSWORD)
            .setClientId(CLIENT_ID).setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build().create(SymptomManagementApi.class);

    private SymptomManagementApi readOnlySmService = new SecuredRestBuilder()
            .setLoginEndpoint(TEST_URL + SymptomManagementApi.TOKEN_PATH).setUsername(USERNAME).setPassword(PASSWORD)
            .setClientId(READ_ONLY_CLIENT_ID).setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build().create(SymptomManagementApi.class);

    private SymptomManagementApi invalidClientVideoService = new SecuredRestBuilder()
            .setLoginEndpoint(TEST_URL + SymptomManagementApi.TOKEN_PATH).setUsername(UUID.randomUUID().toString())
            .setPassword(UUID.randomUUID().toString()).setClientId(UUID.randomUUID().toString())
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient())).setEndpoint(TEST_URL)
            .setLogLevel(LogLevel.FULL).build().create(SymptomManagementApi.class);

    private Patient patient = TestData.randomPatient();

    /**
     * This test creates a Patient, adds the Patient to the
     * SymptomManagementApi, and then checks that the Patient is included in the
     * list when getPatientList() is called.
     * 
     * @throws Exception
     */
    @Test
    public void testPatientAddAndList() throws Exception {
        // Add the video
        smService.addPatient(patient);

        // We should get back the patients that we added above
        Collection<Patient> patients = smService.getPatientList();
        assertTrue(patients.contains(patient));
    }

    /**
     * This test ensures that clients with invalid credentials cannot get access
     * to patients.
     * 
     * @throws Exception
     */
    @Test
    public void testAccessDeniedWithIncorrectCredentials() throws Exception {

        try {
            // Add the video
            invalidClientVideoService.addPatient(patient);

            fail("The server should have prevented the client from adding a video"
                    + " because it presented invalid client/user credentials");
        } catch (RetrofitError e) {
            assert (e.getCause() instanceof SecuredRestException);
        }
    }

    /**
     * This test ensures that read-only clients can access the patient list but
     * not add new patients.
     * 
     * @throws Exception
     */
    @Test
    public void testReadOnlyClientAccess() throws Exception {

        Collection<Patient> videos = readOnlySmService.getPatientList();
        assertNotNull(videos);

        try {
            // Add the patient
            readOnlySmService.addPatient(patient);

            fail("The server should have prevented the client from adding a video"
                    + " because it is using a read-only client ID");
        } catch (RetrofitError e) {
            JsonObject body = (JsonObject) e.getBodyAs(JsonObject.class);
            assertEquals("access_denied", body.get("error").getAsString());
        }
    }

    @Test
    public void testSearchByName() throws Exception {
        Collection<Patient> patients = smService.findByName("name");
        assertNotNull(patients);
    }

}
