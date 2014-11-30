package org.coursera.capstone.android.activity;

import android.app.FragmentTransaction;
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
import org.coursera.capstone.android.fragment.CheckInSummaryFragment;
import org.coursera.capstone.android.fragment.PatientCheckInFragment;
import org.coursera.capstone.android.fragment.PatientSettingsFragment;
import org.coursera.capstone.android.fragment.QuestionFragment;
import org.coursera.capstone.android.fragment.WelcomePatientFragment;
import org.coursera.capstone.android.parcelable.Answer;
import org.coursera.capstone.android.parcelable.CheckIn;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.CheckInRequestTask;
import org.coursera.capstone.android.task.FetchPatientInfoTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatientMainActivity extends FragmentActivity implements FetchPatientInfoTask.PatientInfoCallbacks, QuestionFragment.OnQuestionAnsweredListener,
        CheckInSummaryFragment.OnCheckInSubmitListener {

    private Patient mPatient;
    private User mUser;
    // The check in
    private CheckIn mCheckIn;
    // The check in alarms
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
        // Initialise the check in request
        mCheckIn = new CheckIn();
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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.patient_fragment_container, new PatientSettingsFragment())
                    .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.patient_fragment_container, WelcomePatientFragment.newInstance(mPatient))
                .commit();
    }

    @Override
    public void onPatientFetchFail(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
        // Reset user and start login activity
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(CapstoneConstants.PREFERENCES_USER, "").commit();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onCheckInSubmit() {
        mCheckIn.setPatient(mPatient);
        new CheckInRequestTask(mUser.getAccessToken()).execute(mCheckIn.toRequest());
        // Close the app
        finish();
    }

    @Override
    public void onQuestionAnswered(Question question, Answer answer) {
        CheckIn.QuestionAnswer qa = new CheckIn.QuestionAnswer(question, answer);
        // Update answer if this already exists
        if (mCheckIn.getQuestionAnswers().contains(qa)) {
            int idx = mCheckIn.getQuestionAnswers().indexOf(qa);
            mCheckIn.getQuestionAnswers().set(idx, qa);
        } else {
            mCheckIn.getQuestionAnswers().add(new CheckIn.QuestionAnswer(question, answer));
        }
        // Next question
        PatientCheckInFragment checkInFragment = (PatientCheckInFragment)
                getSupportFragmentManager().findFragmentByTag(PatientCheckInFragment.TAG);
        checkInFragment.nextQuestion(mCheckIn);

    }

    @Override
    public void onMedicalQuestionAnswered(PainMedication painMedication, Date when) {
        CheckIn.MedicationTaken mt = new CheckIn.MedicationTaken(painMedication,
                when != null ? when.getTime() : null);
        // Replace if already exists
        if (mCheckIn.getMedicationsTaken().contains(mt)) {
            int idx = mCheckIn.getMedicationsTaken().indexOf(mt);
            mCheckIn.getMedicationsTaken().set(idx, mt);
        } else {
            mCheckIn.getMedicationsTaken().add(mt);
        }
        // Next question
        PatientCheckInFragment checkInFragment = (PatientCheckInFragment)
                getSupportFragmentManager().findFragmentByTag(PatientCheckInFragment.TAG);
        checkInFragment.nextQuestion(mCheckIn);
    }

    @Override
    public void onMedicalNoMedicationsTaken() {
        // Patient didn't take any medications
        mCheckIn.getMedicationsTaken().clear();
        for (PainMedication pm : mPatient.getMedications()) {
            mCheckIn.getMedicationsTaken().add(new CheckIn.MedicationTaken(pm, null));
        }
        // Don't the pain medication questions
        PatientCheckInFragment checkInFragment = (PatientCheckInFragment)
                getSupportFragmentManager().findFragmentByTag(PatientCheckInFragment.TAG);
        checkInFragment.dontAskPainMedicationQuestions();
    }

    @Override
    public void onMedicationsTaken(Date when) {
        PatientCheckInFragment checkInFragment = (PatientCheckInFragment)
                getSupportFragmentManager().findFragmentByTag(PatientCheckInFragment.TAG);
        // If only one medication, there's no need to ask about each one
        if (mPatient.getMedications().size() == 1) {
            mCheckIn.getMedicationsTaken().clear();
            mCheckIn.getMedicationsTaken().add(new CheckIn.MedicationTaken(mPatient.getMedications().get(0), null));
            checkInFragment.dontAskPainMedicationQuestions();
        } else {
            // Need to ask for each pain medication
            checkInFragment.nextQuestion(mCheckIn);
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
