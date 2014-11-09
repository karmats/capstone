package org.coursera.capstone.android.task;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.client.UnsafeHttpClient;
import org.coursera.capstone.android.parceable.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, LoginTask.Result> {

    private final String mUsername;
    private final String mPassword;
    private final LoginCallbacks mCallbacks;
    // Created logged in user on success
    private User mUser;

    public LoginTask(String username, String password, LoginCallbacks callbacks) {
        mUsername = username;
        mPassword = password;
        mCallbacks = callbacks;
    }

    @Override
    protected Result doInBackground(Void... params) {

        // Assume the result is wrong username or password
        Result result = Result.WRONG_USERNAME_PASSWORD;
        try {
            HttpClient client = new UnsafeHttpClient();
            HttpPost httpPost = new HttpPost(SymptomManagementApi.HOST + SymptomManagementApi.TOKEN_PATH);
            // Request parameters
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", mUsername));
            pairs.add(new BasicNameValuePair("password", mPassword));

            // Add the client ID and client secret to the body of the request.
            pairs.add(new BasicNameValuePair("client_id", CapstoneConstants.OAUTH_CLIENT));
            pairs.add(new BasicNameValuePair("client_secret", ""));
            // Indicate that we're using the OAuth Password Grant Flow
            // by adding grant_type=password to the body
            pairs.add(new BasicNameValuePair("grant_type", "password"));
            // The password grant requires BASIC authentication of the client.
            // In order to do BASIC authentication, we need to concatenate the
            // client_id and client_secret values together with a colon and then
            // Base64 encode them. The final value is added to the request as
            // the "Authorization" header and the value is set to "Basic "
            // concatenated with the Base64 client_id:client_secret value described
            // above.
            String base64Auth = Base64.encodeToString(new String(CapstoneConstants.OAUTH_CLIENT + ":").getBytes(), Base64.NO_WRAP);
            // Add the basic authorization header
            httpPost.setHeader(new BasicHeader("Authorization", "Basic " + base64Auth));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            Log.i(CapstoneConstants.LOG_TAG, "Preparing to execute " + EntityUtils.toString(new UrlEncodedFormEntity(pairs)));

            /// Execute the request and handle the response
            HttpResponse response = client.execute(httpPost);
            result = Result.fromHttpStatus(response.getStatusLine().getStatusCode());
            if (result == Result.SUCCESS) {
                HttpEntity entity = response.getEntity();
                String respString = entity != null ? EntityUtils.toString(entity) : null;
                try {
                    // The expected response should be of JSON type
                    JSONObject json = respString != null ? new JSONObject(respString) : null;
                    // Success we got the access token
                    String accessToken = json.get("access_token").toString();
                    mUser = new User(mUsername, "", "", accessToken);
                    Log.i(CapstoneConstants.LOG_TAG, "Response " + json.get("access_token"));
                } catch (JSONException e) {
                    result = Result.WRONG_USERNAME_PASSWORD;
                }
            }

        } catch (IOException e) {
            Log.e(CapstoneConstants.LOG_TAG, Log.getStackTraceString(e));
        }

        return result;
    }

    @Override
    protected void onPostExecute(final Result result) {
        if (result == LoginTask.Result.SUCCESS) {
            mCallbacks.onLoginSuccess(mUser);
        } else {
            mCallbacks.onLoginError(result);
        }
    }

    @Override
    protected void onCancelled() {
        mCallbacks.onLoginCancel();
    }

    /**
     * Login result, depending on the http status code returned.
     */
    public enum Result {

        SUCCESS,
        WRONG_USERNAME_PASSWORD,
        SERVER_ERROR;

        static Result fromHttpStatus(int httpStatus) {
            // Consider as success if between 200-299
            if (httpStatus >= 200 && httpStatus < 300) {
                return SUCCESS;
            } else if (httpStatus == 401) {
                // Unauthorized
                return WRONG_USERNAME_PASSWORD;
            } else if (httpStatus >= 300) {
                // If status is more than 300, consider this as a server error
                return SERVER_ERROR;
            }
            return WRONG_USERNAME_PASSWORD;
        }

    }

    /**
     * Interface for callback methods when the task is done and when it's cancelled
     */
    public interface LoginCallbacks {
        /**
         * Successful login
         *
         * @param user The responded access token
         */
        void onLoginSuccess(User user);

        /**
         * Failed login attempt.
         *
         * @param error The Result error
         */
        void onLoginError(Result error);

        /**
         * Login was cancelled.
         */
        void onLoginCancel();
    }
}