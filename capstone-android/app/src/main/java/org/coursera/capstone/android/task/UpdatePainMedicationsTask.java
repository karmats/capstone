package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;

import java.util.List;

import retrofit.client.Response;

/**
 * Task to update pain medications for a patient
 */
public class UpdatePainMedicationsTask extends AsyncTask<UpdatePainMedicationsTask.UpdatePainMedicationRequest, Void, UpdatePainMedicationsTask.UpdatePainMedicationRequest> {

    private String mAccessToken;
    private UpdatePainMedicationsCallback mCallback;

    public UpdatePainMedicationsTask(String accessToken, UpdatePainMedicationsCallback callback) {
        this.mAccessToken = accessToken;
        this.mCallback = callback;
    }

    @Override
    protected UpdatePainMedicationRequest doInBackground(UpdatePainMedicationRequest... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        Response response = api.updatePainMedications(params[0].mPatient.getUsername(), params[0].mUpdatedPainMedications);
        if (response.getStatus() >= 200 || response.getStatus() < 300) {
            return params[0];
        }
        return null;
    }

    @Override
    protected void onPostExecute(UpdatePainMedicationRequest request) {
        if (request != null) {
            mCallback.onUpdateMedicationsSuccess(request.mPatient, request.mUpdatedPainMedications);
        } else {
            mCallback.onUpdateMedicationsFailure("Failed to update medications");
        }
    }

    /**
     * Simple class to hold the sending data
     */
    public static class UpdatePainMedicationRequest {
        private final Patient mPatient;
        private final List<PainMedication> mUpdatedPainMedications;

        public UpdatePainMedicationRequest(Patient patient, List<PainMedication> painMedications) {
            this.mPatient = patient;
            this.mUpdatedPainMedications = painMedications;
        }
    }

    public interface UpdatePainMedicationsCallback {
        void onUpdateMedicationsSuccess(Patient patient, List<PainMedication> painMedications);

        void onUpdateMedicationsFailure(String error);
    }
}
