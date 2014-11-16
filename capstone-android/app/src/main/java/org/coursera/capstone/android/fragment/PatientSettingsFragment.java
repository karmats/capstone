package org.coursera.capstone.android.fragment;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.preference.TimePreference;


/**
 * Patient settings fragment
 */
public class PatientSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.patient_preferences);
        PreferenceGroup group = (PreferenceGroup) getPreferenceScreen().getPreference(1);

        group.addPreference(createPreferenceScreen());
    }

    private PreferenceScreen createPreferenceScreen() {
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(getActivity());
        root.addPreference(new TimePreference(getActivity()));
        root.addPreference(new TimePreference(getActivity()));
        root.addPreference(new TimePreference(getActivity()));
        root.addPreference(new TimePreference(getActivity()));
        return root;
    }

}
