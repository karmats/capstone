package org.coursera.capstone.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.LoginTask;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoginTask.LoginCallbacks {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new LoginTask(username, password, this);
        mAuthTask.execute();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    // Override methods from LoginTask.LoginCallbacks interface
    @Override
    public void onLoginSuccess(User user) {
        cancelTask();
        Log.i(CapstoneConstants.LOG_TAG, "Login success, got token " + user.getAccessToken());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        // Add the user object to shared preferences
        preferences.edit().putString(CapstoneConstants.PREFERENCES_USER, user.toJson()).commit();
        // Start doctor or patient activity
        if (CapstoneConstants.PATIENT_ROLE.equals(user.getRole())) {
            startActivity(new Intent(this, PatientMainActivity.class));
        } else if (CapstoneConstants.DOCTOR_ROLE.equals(user.getRole())) {
            startActivity(new Intent(this, DoctorMainActivity.class));
        } else {
            Log.e(CapstoneConstants.LOG_TAG, "Role " + user.getRole() + " is not supported");
        }
        // Finish the login task so it won't come up on back button
        finish();
    }

    public void onLoginError(LoginTask.Result error) {
        cancelTask();
        String errorString = "";
        switch (error) {
            case WRONG_USERNAME_PASSWORD:
                errorString = getString(R.string.error_incorrect_username_or_password);
                break;
            case SERVER_ERROR:
                errorString = getString(R.string.error_server);
                break;
            default:
                errorString = getString(R.string.error_incorrect_username_or_password);
                break;

        }
        mUsernameView.setError(errorString);
        mUsernameView.requestFocus();
    }

    @Override
    public void onLoginCancel() {
        cancelTask();
    }

    private void cancelTask() {
        mAuthTask = null;
        showProgress(false);
    }

}



