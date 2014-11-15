package org.coursera.capstone.android.task;

import android.os.AsyncTask;
import android.util.Log;

import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parceable.Patient;

/**
 * Fetches information for a patient.
 * <p/>
 * The task needs the username of the patient to fetch information for
 */
public class FetchPatientInfoTask extends AsyncTask<String, Void, Patient> {

    private UserDataCallbacks mCallbacks;
    private String mAccessToken;

    public FetchPatientInfoTask(UserDataCallbacks callbacks, String accessToken) {
        this.mCallbacks = callbacks;
        this.mAccessToken = accessToken;
    }

    @Override
    protected Patient doInBackground(String... params) {
        Log.i(CapstoneConstants.LOG_TAG, "Fetching patient info with username " + params[0]);
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.getPatientInformation(params[0]);
    }

    @Override
    protected void onPostExecute(Patient patient) {
        if (patient == null) {
            mCallbacks.onPatientFetchFail("No patient found :(");
        } else {
            mCallbacks.onPatientFetched(patient);
        }
    }

    /**
     * Interface to implement for calling context
     */
    public interface UserDataCallbacks {

        /**
         * When patient data has been fetched.
         *
         * @param p The fetched patient
         */
        void onPatientFetched(Patient p);

        /**
         * If something goes wrong, we got more than one patient or no patient,
         *
         * @param error The error message
         */
        void onPatientFetchFail(String error);
    }
}
