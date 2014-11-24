package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.CheckInResponse;

import java.util.List;

/**
 * Fetches check-ins for a specific patient
 */
public class CheckInsForPatientTask extends AsyncTask<String, Void, List<CheckInResponse>> {

    private String mAccessToken;
    private CheckInsForPatientCallback mCallback;

    public CheckInsForPatientTask(String accessToken, CheckInsForPatientCallback callback) {
        this.mAccessToken = accessToken;
        this.mCallback = callback;
    }

    @Override
    protected List<CheckInResponse> doInBackground(String... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.getPatientCheckIn(params[0]);
    }

    @Override
    protected void onPostExecute(List<CheckInResponse> checkInResponseList) {
        mCallback.onCheckInsForPatientSuccess(checkInResponseList);
    }

    /**
     * Interface to implement for calling context
     */
    public interface CheckInsForPatientCallback {
        void onCheckInsForPatientSuccess(List<CheckInResponse> checkInResponseList);
    }
}
