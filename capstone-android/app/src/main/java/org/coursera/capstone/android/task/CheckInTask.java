package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.CheckIn;

import retrofit.client.Response;

/**
 * Task for posting a check in
 */
public class CheckInTask extends AsyncTask<CheckIn, Void, Boolean> {

    private String mAccessToken;

    public CheckInTask(String accessToken) {
        this.mAccessToken = accessToken;
    }

    @Override
    protected Boolean doInBackground(CheckIn... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        Response response = api.checkIn(params[0]);
        return response.getStatus() == 200;
    }
}
