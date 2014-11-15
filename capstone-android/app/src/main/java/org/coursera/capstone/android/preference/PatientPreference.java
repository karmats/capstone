package org.coursera.capstone.android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parceable.User;

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
        String userJson = getSharedPreferences().getString(CapstoneConstants.PREFERENCES_USER, "");
        User user = User.fromJsonString(userJson);
        String key = getKey();
        if (key.equals("name")) {
            return user.getFirstName() + " " + user.getLastName();
        } else {
            Log.e(CapstoneConstants.LOG_TAG, "Not supported patient field " + key);
        }
        return "";
    }

}