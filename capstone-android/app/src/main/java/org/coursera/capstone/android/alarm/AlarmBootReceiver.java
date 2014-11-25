package org.coursera.capstone.android.alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.coursera.capstone.android.activity.PatientMainActivity;

import java.util.Calendar;
import java.util.List;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted.
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    CheckInAlarmReceiver mAlarm = new CheckInAlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<Calendar> alarms = PatientMainActivity.getAlarms(context);
            for (int i = 0; i < alarms.size(); i++) {
                // The pending intent with id to execute on the selected time
                Intent alarmIntent = new Intent(context, CheckInAlarmReceiver.class);
                PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mAlarm.setAlarm(context, pendingAlarmIntent, alarms.get(i));
            }
        }
    }
}
