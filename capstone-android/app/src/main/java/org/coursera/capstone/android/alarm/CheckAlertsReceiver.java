package org.coursera.capstone.android.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.service.CheckAlertsService;

import java.util.ArrayList;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class CheckAlertsReceiver extends WakefulBroadcastReceiver {
    // The pending intent request id
    public static final int REQUEST_ID = 15;
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(CapstoneConstants.LOG_TAG, "Receiving intent and starting service..");
        Intent service = new Intent(context, CheckAlertsService.class);
        service.putExtras(intent);

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
    }

    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context, ArrayList<String> patientUserNames, String accessToken) {
        Log.i(CapstoneConstants.LOG_TAG, "Setting up check alerts");
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        Intent intent = new Intent(context, CheckAlertsReceiver.class);
        intent.putStringArrayListExtra(CheckAlertsService.PATIENTS_PARAM, patientUserNames);
        intent.putExtra(CheckAlertsService.ACCESS_TOKEN_PARAM, accessToken);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_ID, intent, 0);

        // Set the alarm to fire every half hour
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);

        // Enable AlarmBootReceiver to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        if (pm.getComponentEnabledSetting(receiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    /**
     * Cancel this alarm
     *
     * @param context
     */
    public void cancelAlarm(Context context) {
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        Intent intent = new Intent(context, CheckAlertsReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_ID, intent, 0);
        alarmIntent.cancel();
        alarmMgr.cancel(alarmIntent);
    }

}
