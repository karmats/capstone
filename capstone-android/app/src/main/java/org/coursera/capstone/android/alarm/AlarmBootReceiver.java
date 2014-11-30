package org.coursera.capstone.android.alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.coursera.capstone.android.activity.PatientMainActivity;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted.
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    CheckInAlarmReceiver mCheckInAlarm = new CheckInAlarmReceiver();
    CheckAlertsReceiver mCheckAlerts = new CheckAlertsReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<Calendar> alarms = PatientMainActivity.getAlarms(context);
            // Restart check in alarms
            mCheckInAlarm.cancelAlarm(context, alarms.size());
            for (int i = 0; i < alarms.size(); i++) {
                // The pending intent with id to execute on the selected time
                Intent alarmIntent = new Intent(context, CheckInAlarmReceiver.class);
                PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mCheckInAlarm.setAlarm(context, pendingAlarmIntent, alarms.get(i));
            }
            // Restart check alerts
            mCheckAlerts.cancelAlarm(context);
            // Get the user information from shared preferences
            String userJsonString = PreferenceManager.getDefaultSharedPreferences(context).getString(CapstoneConstants.PREFERENCES_USER, "");
            User user = User.fromJsonString(userJsonString);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> patientUserNames = preferences.getStringSet(CapstoneConstants.PREFERENCES_DOCTOR_PATIENT_USERNAMES, new HashSet<String>());
            mCheckAlerts.setAlarm(context, new ArrayList<String>(patientUserNames), user.getAccessToken());
        }
    }
}
