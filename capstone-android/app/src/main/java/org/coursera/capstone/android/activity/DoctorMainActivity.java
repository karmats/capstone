package org.coursera.capstone.android.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.fragment.DoctorPatientDetailsFragment;
import org.coursera.capstone.android.fragment.ListDoctorPatientsFragment;
import org.coursera.capstone.android.fragment.UpdateMedicationsFragment;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.FetchDoctorPatientsTask;
import org.coursera.capstone.android.task.FetchPainMedicationsTask;

import java.util.ArrayList;
import java.util.List;

public class DoctorMainActivity extends FragmentActivity implements FetchDoctorPatientsTask.DoctorPatientsCallbacks,
        ListDoctorPatientsFragment.OnPatientSelectedListener, UpdateMedicationsFragment.OnUpdateMedicationsListener,
        FetchPainMedicationsTask.FetchPainMedicationsCallback {

    private User mUser;
    private Patient mCurrentPatient;
    private ArrayList<PainMedication> mAllPainMedications;

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

    // Callbacks for fetching doctor patients
    @Override
    public void onPatientsFetched(List<Patient> patients) {
        Log.i(CapstoneConstants.LOG_TAG, "Success got " + patients.size() + " patients");
        getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                ListDoctorPatientsFragment.newInstance(new ArrayList<Patient>(patients))).addToBackStack(null)
                .commit();
    }

    @Override
    public void onPatientsFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onPatientSelected(Patient patient) {
        Log.i(CapstoneConstants.LOG_TAG, patient.getFirstName() + " selected");
        this.mCurrentPatient = patient;
        if (mAllPainMedications == null) {
            new FetchPainMedicationsTask(mUser.getAccessToken(), this).execute();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                    DoctorPatientDetailsFragment.newInstance(mCurrentPatient, mAllPainMedications)).addToBackStack(null).commit();
        }
    }

    @Override
    public void onPainMedicationsSuccess(List<PainMedication> medications) {
        mAllPainMedications = new ArrayList<PainMedication>(medications);
        getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                DoctorPatientDetailsFragment.newInstance(mCurrentPatient, mAllPainMedications)).addToBackStack(null).commit();
    }

    @Override
    public void onPainMedicationsError(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onMedicationUpdate(Patient patient) {
        Log.i(CapstoneConstants.LOG_TAG, "Medications update");
    }

}
