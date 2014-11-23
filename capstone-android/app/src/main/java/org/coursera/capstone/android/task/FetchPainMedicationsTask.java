package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.PainMedication;

import java.util.List;

/**
 * Fetches all available pain medications
 */
public class FetchPainMedicationsTask extends AsyncTask<Void, Void, List<PainMedication>> {

    private String mAccessToken;
    private FetchPainMedicationsCallback mCallback;

    public FetchPainMedicationsTask(String accessToken, FetchPainMedicationsCallback callback) {
        this.mAccessToken = accessToken;
        this.mCallback = callback;
    }

    @Override
    protected List<PainMedication> doInBackground(Void... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.getPainMedications();
    }

    @Override
    protected void onPostExecute(List<PainMedication> medications) {
        if (medications.size() <= 0) {
            mCallback.onPainMedicationsError("Failed to fetch medications");
        } else {
            mCallback.onPainMedicationsSuccess(medications);
        }
    }

    public interface FetchPainMedicationsCallback {
        void onPainMedicationsSuccess(List<PainMedication> medications);

        void onPainMedicationsError(String error);
    }
}
