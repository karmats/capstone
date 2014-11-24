package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.CheckInRequest;

import retrofit.client.Response;

/**
 * Task for posting a check in
 */
public class CheckInRequestTask extends AsyncTask<CheckInRequest, Void, Boolean> {

    private String mAccessToken;

    public CheckInRequestTask(String accessToken) {
        this.mAccessToken = accessToken;
    }

    @Override
    protected Boolean doInBackground(CheckInRequest... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        Response response = api.checkIn(params[0]);
        return response.getStatus() == 200;
    }
}
