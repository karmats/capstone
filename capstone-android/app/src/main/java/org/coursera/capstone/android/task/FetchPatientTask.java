package org.coursera.capstone.android.task;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parceable.Patient;
import org.coursera.capstone.android.parceable.User;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Fetch information for an authenticated patient
 */
public class FetchPatientTask extends AsyncTask<Void, Void, List<Patient>> {

    private TextView mTextView;
    // Api
    private SymptomManagementApi api;

    public FetchPatientTask(Context context, TextView textView) {
        this.mTextView = textView;
        String userJsonString = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(CapstoneConstants.PREFERENCES_USER, "");
        User user = User.fromJsonString(userJsonString);
        Log.d(CapstoneConstants.LOG_TAG, "Fetching patients with access token " + user.getAccessToken());
        this.api = SymptomManagementApiBuilder.newInstance(user.getAccessToken());
    }

    @Override
    protected void onPostExecute(List<Patient> patients) {
        for (Patient p : patients) {
            mTextView.append(p.getFirstName() + " " + p.getLastName() + "\n");
        }
    }

    @Override
    protected List<Patient> doInBackground(Void... params) {
        List<Patient> result = new ArrayList<Patient>();
        try {
            result = api.getPatientList();
        } catch (RetrofitError e) {
            Log.wtf(CapstoneConstants.LOG_TAG, e);
        }
        Log.i(CapstoneConstants.LOG_TAG, "Got list of " + result.size());
        return result;
    }

}
