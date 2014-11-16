package org.coursera.capstone.android.fragment;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.preference.TimePreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Patient settings fragment
 */
public class PatientSettingsFragment extends PreferenceFragment {

    private PreferenceCategory mReminderPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.patient_preferences);
        mReminderPreference = (PreferenceCategory) getPreferenceScreen().getPreference(1);
        createPreferenceScreen();
    }

    private void createPreferenceScreen() {
        Set<String> reminders = getPreferenceManager().getSharedPreferences().getStringSet(
                CapstoneConstants.PREFERENCES_PATIENT_REMINDERS, createDefaultReminderSettings());
        List<String> sortedReminders = new ArrayList<String>(reminders);
        Collections.sort(sortedReminders);
        for (String reminder : sortedReminders) {
            mReminderPreference.addPreference(new TimePreference(getActivity(), reminder));
        }
        mReminderPreference.setTitle(getString(R.string.pref_title_alarms));
    }

    private Set<String> createDefaultReminderSettings() {
        Set<String> result = new HashSet<String>();
        result.add("08:00");
        result.add("12:00");
        result.add("16:00");
        result.add("20:00");
        return result;
    }

    @Override
    public void onDestroy() {
        // Save the reminder preferences
        Set<String> reminders = new HashSet<String>();
        for (int i = 0; i < mReminderPreference.getPreferenceCount(); i++) {
            TimePreference tp = (TimePreference) mReminderPreference.getPreference(i);
            reminders.add(tp.getTimeValue());
        }
        getPreferenceManager().getSharedPreferences().edit().putStringSet(
                CapstoneConstants.PREFERENCES_PATIENT_REMINDERS, reminders).commit();
        super.onDestroy();
    }

}
