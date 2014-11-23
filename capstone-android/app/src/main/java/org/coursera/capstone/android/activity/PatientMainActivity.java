package org.coursera.capstone.android.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.alarm.CheckInAlarmReceiver;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.fragment.CheckInFragment;
import org.coursera.capstone.android.fragment.PatientSettingsFragment;
import org.coursera.capstone.android.fragment.WelcomePatientFragment;
import org.coursera.capstone.android.parcelable.CheckIn;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.CheckInTask;
import org.coursera.capstone.android.task.FetchPatientInfoTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatientMainActivity extends FragmentActivity implements FetchPatientInfoTask.PatientInfoCallbacks, CheckInFragment.OnQuestionsAnsweredListener {

    private Patient mPatient;
    private User mUser;
    private CheckInAlarmReceiver mCheckInAlarm = new CheckInAlarmReceiver();
    // Listener for patient reminders changes
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        // Register a listener for shared preference updates, there is a need for setting up the alarms
        // again if the user has changed the times
        mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (CapstoneConstants.PREFERENCES_PATIENT_REMINDERS.equals(key)) {
                    Log.i(CapstoneConstants.LOG_TAG, "Patient has changed the reminders, resetting the alarms");
                    setupCheckInAlarms();
                }
            }
        };
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
        // Get the user information from shared preferences
        String userJsonString = sharedPreferences.getString(CapstoneConstants.PREFERENCES_USER, "");
        mUser = User.fromJsonString(userJsonString);

        // Fetch information about the patient, such as medical record no and birth date
        new FetchPatientInfoTask(this, mUser.getAccessToken()).execute(mUser.getUsername());
        // Setup the check-in alarms
        setupCheckInAlarms();
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
                    .replace(R.id.patient_fragment_container, new PatientSettingsFragment())
                    .addToBackStack(null).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPatientFetched(Patient p) {
        mPatient = p;
        // Store the patient in preference editor
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefEditor.putString(CapstoneConstants.PREFERENCES_NAME, p.getFirstName() + " " + p.getLastName());
        prefEditor.putString(CapstoneConstants.PREFERENCES_DATE_OF_BIRTH,
                DateFormat.getDateFormat(PatientMainActivity.this).format(new Date(p.getBirthDate())));
        prefEditor.putLong(CapstoneConstants.PREFERENCES_MEDICAL_RECORD_NUMBER, p.getMedicalRecordNumber());
        prefEditor.putString(CapstoneConstants.PREFERENCES_PATIENT_DOCTOR_NAME, p.getDoctor().getFirstName() +
                " " + p.getDoctor().getLastName());
        prefEditor.commit();
        // Start the welcome fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.patient_fragment_container, WelcomePatientFragment.newInstance(mPatient))
                .commit();
    }

    @Override
    public void onPatientFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

    @Override
    public void onAllQuestionsAnswered(CheckIn checkInData) {
        new CheckInTask(mUser.getAccessToken()).execute(checkInData);
        //finish();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Sets up the check in alarms based on the users preference
     */
    private void setupCheckInAlarms() {
        List<Calendar> alarms = getAlarms(this);
        for (int i = 0; i < alarms.size(); i++) {
            // The pending intent with id to execute on the selected time
            Intent intent = new Intent(this, CheckInAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, i, intent, 0);
            mCheckInAlarm.setAlarm(this, alarmIntent, alarms.get(i));
        }
    }

    /**
     * Get a list with alarm dates
     *
     * @param ctx
     * @return A list of Calendar objects
     */
    public static List<Calendar> getAlarms(Context ctx) {
        List<Calendar> result = new ArrayList<Calendar>();
        Set<String> alarmSet = PreferenceManager.getDefaultSharedPreferences(ctx)
                .getStringSet(CapstoneConstants.PREFERENCES_PATIENT_REMINDERS, new HashSet<String>());
        String[] alarms = alarmSet.toArray(new String[alarmSet.size()]);
        for (int i = 0; i < alarms.length; i++) {
            // The time is in format HH:mm
            String[] hourMinute = alarms[i].split(":");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinute[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(hourMinute[1]));
            // If the time is in the pass, we need to add an extra day so the intent won't be executed
            // right away
            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, c.get(Calendar.DATE) + 1);
            }
            result.add(c);
        }
        return result;
    }
}
