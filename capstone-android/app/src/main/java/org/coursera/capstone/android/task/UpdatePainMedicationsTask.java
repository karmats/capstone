package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.PainMedication;

import java.util.List;

import retrofit.client.Response;

/**
 * Task to update pain medications for a patient
 */
public class UpdatePainMedicationsTask extends AsyncTask<UpdatePainMedicationsTask.UpdatePainMedicationRequest, Void, Integer> {

    private String mAccessToken;
    private UpdatePainMedicationsCallback mCallback;

    public UpdatePainMedicationsTask(String accessToken, UpdatePainMedicationsCallback callback) {
        this.mAccessToken = accessToken;
        this.mCallback = callback;
    }

    @Override
    protected Integer doInBackground(UpdatePainMedicationRequest... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        Response response = api.updatePainMedications(params[0].mPatientUsername, params[0].mUpdatedPainMedications);
        return response.getStatus();
    }

    @Override
    protected void onPostExecute(Integer status) {
        if(status >= 200 || status < 300) {
            mCallback.onUpdateMedicationsSuccess();
        } else {
            mCallback.onUpdateMedicationsFailure("Failed to update medications got http status " + status);
        }
    }

    /**
     * Simple class to hold the sending data
     */
    public static class UpdatePainMedicationRequest {
        private final String mPatientUsername;
        private final List<PainMedication> mUpdatedPainMedications;

        public UpdatePainMedicationRequest(String patientUsername, List<PainMedication> painMedications) {
            this.mPatientUsername = patientUsername;
            this.mUpdatedPainMedications = painMedications;
        }
    }

    public interface UpdatePainMedicationsCallback {
        void onUpdateMedicationsSuccess();
        void onUpdateMedicationsFailure(String error);
    }
}
