package org.coursera.capstone.android.task;

import android.os.AsyncTask;
import android.util.Log;

import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parceable.Patient;

import java.util.List;

/**
 * Fetches information for a patient.
 * <p/>
 * The task needs the username of the patient to fetch information for
 */
public class FetchPatientInfoTask extends AsyncTask<String, Void, List<Patient>> {

    private UserDataCallbacks mCallbacks;
    private String mAccessToken;

    public FetchPatientInfoTask(UserDataCallbacks callbacks, String accessToken) {
        this.mCallbacks = callbacks;
        this.mAccessToken = accessToken;
    }

    @Override
    protected List<Patient> doInBackground(String... params) {
        Log.i(CapstoneConstants.LOG_TAG, "Fetching patient info with username " + params[0]);
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.findPatientByUsername(params[0]);
    }

    @Override
    protected void onPostExecute(List<Patient> patients) {
        if (patients.size() <= 0) {
            mCallbacks.onPatientFetchFail("No patient found :(");
        } else if (patients.size() > 1) {
            mCallbacks.onPatientFetchFail("Got more than one patient");
        } else {
            mCallbacks.onPatientFetched(patients.get(0));
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
