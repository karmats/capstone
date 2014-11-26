package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.Patient;

import java.util.List;

/**
 * Task to search for patients by name
 */
public class SearchPatientsByNameTask extends AsyncTask<String, Void, List<Patient>> {

    private String mAccessToken;
    private OnSearchPatientsByNameCallback mCallback;

    public SearchPatientsByNameTask(String accessToken, OnSearchPatientsByNameCallback callback) {
        this.mAccessToken = accessToken;
        this.mCallback = callback;
    }

    @Override
    protected List<Patient> doInBackground(String... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.findPatientsByName(params[0]);
    }

    @Override
    protected void onPostExecute(List<Patient> patients) {
        mCallback.onSearchPatientsByNameSuccess(patients);
    }

    public interface OnSearchPatientsByNameCallback {
        void onSearchPatientsByNameSuccess(List<Patient> patients);
    }
}
