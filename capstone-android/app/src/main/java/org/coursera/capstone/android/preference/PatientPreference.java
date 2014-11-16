package org.coursera.capstone.android.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import org.coursera.capstone.android.constant.CapstoneConstants;

import java.text.NumberFormat;

/**
 * Patient preference class for static information about a patient, such as name.
 * This is not recommended by the android guidelines, but it felt overkill to have one about and one
 * settings option for this small app.
 */
public class PatientPreference extends Preference {

    public PatientPreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    @Override
    public CharSequence getSummary() {
        String key = getKey();
        // Medical record number is the only long setting we have
        if (CapstoneConstants.PREFERENCES_MEDICAL_RECORD_NUMBER.equals(key)) {
            return NumberFormat.getInstance().format(getPersistedLong(-1));
        }
        return getPersistedString("Failed to find " + key);
    }

}