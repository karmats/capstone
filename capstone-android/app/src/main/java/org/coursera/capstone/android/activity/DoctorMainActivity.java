package org.coursera.capstone.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.fragment.ListDoctorPatientsFragment;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.FetchDoctorPatientsTask;

import java.util.ArrayList;
import java.util.List;

public class DoctorMainActivity extends Activity implements FetchDoctorPatientsTask.DoctorPatientsCallbacks, ListDoctorPatientsFragment.OnPatientSelectedListener {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        // Get the user information from shared preferences
        String userJsonString = PreferenceManager.getDefaultSharedPreferences(DoctorMainActivity.this)
                .getString(CapstoneConstants.PREFERENCES_USER, "");
        mUser = User.fromJsonString(userJsonString);


        new FetchDoctorPatientsTask(this, mUser.getAccessToken()).execute(mUser.getUsername());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Callbacks for fetching doctor patients
    @Override
    public void onPatientsFetched(List<Patient> patients) {
        Log.i(CapstoneConstants.LOG_TAG, "Success got " + patients.size() + " patients");
        getFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                ListDoctorPatientsFragment.newInstance(new ArrayList<Patient>(patients))).commit();
    }

    @Override
    public void onPatientsFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onPatientSelected(Patient patient) {
        Log.i(CapstoneConstants.LOG_TAG, patient.getFirstName() + " selected");
    }
}
