package org.coursera.capstone.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.alarm.CheckAlertsReceiver;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.fragment.DoctorPatientDetailsFragment;
import org.coursera.capstone.android.fragment.ListCheckInsFragment;
import org.coursera.capstone.android.fragment.ListDoctorPatientsFragment;
import org.coursera.capstone.android.fragment.SearchPatientsFragment;
import org.coursera.capstone.android.fragment.UpdateMedicationsFragment;
import org.coursera.capstone.android.parcelable.CheckInResponse;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.CheckInsForPatientTask;
import org.coursera.capstone.android.task.FetchDoctorPatientsTask;
import org.coursera.capstone.android.task.FetchPainMedicationsTask;
import org.coursera.capstone.android.task.UpdatePainMedicationsTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DoctorMainActivity extends FragmentActivity implements FetchDoctorPatientsTask.DoctorPatientsCallbacks,
        ListDoctorPatientsFragment.OnPatientSelectedListener, UpdateMedicationsFragment.OnUpdateMedicationsListener,
        FetchPainMedicationsTask.FetchPainMedicationsCallback, UpdatePainMedicationsTask.UpdatePainMedicationsCallback,
        ListCheckInsFragment.OnCheckInSelectedListener, CheckInsForPatientTask.CheckInsForPatientCallback,
        SearchPatientsFragment.OnSearchPatientSelectedListener {

    private User mUser;
    private Patient mCurrentPatient;
    private ArrayList<Patient> mDoctorsPatients;
    private ArrayList<PainMedication> mAllPainMedications;
    private ArrayList<CheckInResponse> mCheckIns;

    private CheckAlertsReceiver mCheckAlerts = new CheckAlertsReceiver();

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
        ArrayList<Patient> patientArray = new ArrayList<Patient>(patients);
        this.mDoctorsPatients = patientArray;
        Log.i(CapstoneConstants.LOG_TAG, "Success got " + patients.size() + " patients");
        getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                ListDoctorPatientsFragment.newInstance(patientArray)).addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

        // Now when patients are fetched we can start the polling for alerts service
        ArrayList<String> patientUsernames = new ArrayList<String>(patientArray.size());
        for (Patient p : patientArray) {
            patientUsernames.add(p.getUsername());
        }
        // Add doctor patient user names to shared preferences, since on boot we need to read them in CheckAlertsService
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putStringSet(CapstoneConstants.PREFERENCES_DOCTOR_PATIENT_USERNAMES,
                new HashSet<String>(patientUsernames)).commit();
        // Restart the check alert alarm
        mCheckAlerts.cancelAlarm(this);
        mCheckAlerts.setAlarm(this, patientUsernames, mUser.getAccessToken());
    }

    @Override
    public void onPatientsFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(CapstoneConstants.PREFERENCES_USER, "").commit();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onPatientSelected(Patient patient) {
        Log.i(CapstoneConstants.LOG_TAG, patient.getFirstName() + " selected");
        this.mCurrentPatient = patient;
        createDoctorPatientDetailsFragment(true);
    }

    @Override
    public void onPainMedicationsSuccess(List<PainMedication> medications) {
        mAllPainMedications = new ArrayList<PainMedication>(medications);
        createDoctorPatientDetailsFragment(false);
    }

    @Override
    public void onPainMedicationsError(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onMedicationUpdate(Patient patient, List<PainMedication> painMedications) {
        new UpdatePainMedicationsTask(mUser.getAccessToken(), this).execute(
                new UpdatePainMedicationsTask.UpdatePainMedicationRequest(patient, painMedications));
    }

    @Override
    public void onUpdateMedicationsSuccess(Patient patient, List<PainMedication> updatedPainMedications) {
        Log.i(CapstoneConstants.LOG_TAG, "Yay, medication update was a success");
        Toast.makeText(this, getString(R.string.update_medications_success), Toast.LENGTH_SHORT).show();
        patient.setMedications(updatedPainMedications);
    }

    @Override
    public void onUpdateMedicationsFailure(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onCheckInSelected(CheckInResponse checkIn) {
        Log.i(CapstoneConstants.LOG_TAG, checkIn.getWhen() + " selected");
    }

    @Override
    public void onCheckInsForPatientSuccess(List<CheckInResponse> checkInResponseList) {
        this.mCheckIns = new ArrayList<CheckInResponse>(checkInResponseList);
        createDoctorPatientDetailsFragment(false);
    }

    @Override
    public void onSearchedPatientSelected(Patient patient) {
        Log.i(CapstoneConstants.LOG_TAG, patient.getUsername() + " selected from search");
        // Selected show the details fragment
        this.mCurrentPatient = patient;
        createDoctorPatientDetailsFragment(true);
    }

    private void createDoctorPatientDetailsFragment(boolean updateCheckIns) {
        if (mAllPainMedications == null) {
            new FetchPainMedicationsTask(mUser.getAccessToken(), this).execute();
        } else if (mCheckIns == null || updateCheckIns) {
            new CheckInsForPatientTask(mUser.getAccessToken(), this).execute(mCurrentPatient.getUsername());
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                    DoctorPatientDetailsFragment.newInstance(mCurrentPatient, mAllPainMedications, mCheckIns, mDoctorsPatients))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        }
    }
}
