package org.coursera.capstone.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.fragment.PatientSettingsFragment;
import org.coursera.capstone.android.parceable.Patient;
import org.coursera.capstone.android.parceable.User;
import org.coursera.capstone.android.task.FetchPatientInfoTask;

import java.util.Date;

public class PatientMainActivity extends Activity implements FetchPatientInfoTask.PatientInfoCallbacks {

    private Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        // Get the patient name from shared preferences
        String userJsonString = PreferenceManager.getDefaultSharedPreferences(PatientMainActivity.this)
                .getString(CapstoneConstants.PREFERENCES_USER, "");
        User user = User.fromJsonString(userJsonString);

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPatientFetched(Patient p) {
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(PatientMainActivity.this).edit();
        prefEditor.putString(CapstoneConstants.PREFERENCES_NAME, p.getFirstName() + " " + p.getLastName());
        prefEditor.putString(CapstoneConstants.PREFERENCES_DATE_OF_BIRTH,
                DateFormat.getDateFormat(PatientMainActivity.this).format(new Date(p.getBirthDate())));
        prefEditor.putLong(CapstoneConstants.PREFERENCES_MEDICAL_RECORD_NUMBER, p.getMedicalRecordNumber());
        prefEditor.putString(CapstoneConstants.PREFERENCES_PATIENT_DOCTOR_NAME, p.getDoctor().getFirstName() +
                " " + p.getDoctor().getLastName());
        prefEditor.commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PatientSettingsFragment())
                .commit();
    }

    @Override
    public void onPatientFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }
}
