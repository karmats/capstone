package org.coursera.capstone.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.fragment.CheckInFragment;
import org.coursera.capstone.android.fragment.PatientSettingsFragment;
import org.coursera.capstone.android.fragment.WelcomePatientFragment;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.FetchPatientInfoTask;
import org.coursera.capstone.android.task.FetchQuestionsTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientMainActivity extends Activity implements FetchPatientInfoTask.PatientInfoCallbacks, CheckInFragment.OnQuestionsAnsweredListener {

    private Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        // Get the patient name from shared preferences
        String userJsonString = PreferenceManager.getDefaultSharedPreferences(PatientMainActivity.this)
                .getString(CapstoneConstants.PREFERENCES_USER, "");
        final User user = User.fromJsonString(userJsonString);

        // Fetch information about the patient, such as medical record no and birth date
        new FetchPatientInfoTask(this, user.getAccessToken()).execute(user.getUsername());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PatientSettingsFragment())
                    .addToBackStack(null).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPatientFetched(Patient p) {
        mPatient = p;
        // Store the patient in preference editor
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(PatientMainActivity.this).edit();
        prefEditor.putString(CapstoneConstants.PREFERENCES_NAME, p.getFirstName() + " " + p.getLastName());
        prefEditor.putString(CapstoneConstants.PREFERENCES_DATE_OF_BIRTH,
                DateFormat.getDateFormat(PatientMainActivity.this).format(new Date(p.getBirthDate())));
        prefEditor.putLong(CapstoneConstants.PREFERENCES_MEDICAL_RECORD_NUMBER, p.getMedicalRecordNumber());
        prefEditor.putString(CapstoneConstants.PREFERENCES_PATIENT_DOCTOR_NAME, p.getDoctor().getFirstName() +
                " " + p.getDoctor().getLastName());
        prefEditor.commit();
        // Start the welcome fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, WelcomePatientFragment.newInstance(mPatient))
                .commit();
    }

    @Override
    public void onPatientFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onAllQuestionsAnswered() {
        Log.i(CapstoneConstants.LOG_TAG, "All questions answered!");
        finish();
    }
}
