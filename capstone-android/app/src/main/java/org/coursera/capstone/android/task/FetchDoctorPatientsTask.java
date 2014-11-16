package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parceable.Patient;

import java.util.List;

/**
 * Fetches a doctors patients.
 * <p/>
 * The task needs the username of the doctor to fetch patients for
 */
public class FetchDoctorPatientsTask extends AsyncTask<String, Void, List<Patient>> {

    private DoctorPatientsCallbacks mCallbacks;
    private String mAccessToken;

    public FetchDoctorPatientsTask(DoctorPatientsCallbacks callbacks, String accessToken) {
        this.mCallbacks = callbacks;
        this.mAccessToken = accessToken;
    }

    @Override
    protected List<Patient> doInBackground(String... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.getDoctorPatients(params[0]);
    }

    @Override
    protected void onPostExecute(List<Patient> patients) {
        if (patients.size() <= 0) {
            mCallbacks.onPatientsFetchFail("No patients found :(");
        } else {
            mCallbacks.onPatientsFetched(patients);
        }
    }

    /**
     * Interface to implement for calling context
     */
    public interface DoctorPatientsCallbacks {

        /**
         * When patient data has been fetched.
         *
         * @param p The fetched patient
         */
        void onPatientsFetched(List<Patient> p);

        /**
         * If something goes wrong, we got more than one patient or no patient,
         *
         * @param error The error message
         */
        void onPatientsFetchFail(String error);
    }
}
