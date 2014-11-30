package org.coursera.capstone.android.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.activity.PatientMainActivity;
import org.coursera.capstone.android.constant.CapstoneConstants;

import java.util.Calendar;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class CheckInAlarmReceiver extends WakefulBroadcastReceiver {

    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Notify user that it's time for check in
        Log.i(CapstoneConstants.LOG_TAG, "Receiving alarm..");
        sendNotification(context);
    }

    /**
     * Sets a repeating alarm that runs once a day at approximately given time. When the
     * alarm fires, the app broadcasts an the given alarmIntent.
     *
     * @param context     The context
     * @param alarmIntent The pending intent that is triggered when the alarm fires.
     * @param calendar    When the intent should be executed
     */
    public void setAlarm(Context context, PendingIntent alarmIntent, Calendar calendar) {
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

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

    private void sendNotification(Context ctx) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager)
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, PatientMainActivity.class), 0);

        String msg = ctx.getString(R.string.alarm_notification_msg);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(ctx.getString(R.string.alarm_notification_title))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg).setAutoCancel(true);

        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Cancel this alarm. The request ids goes from 0 to the user defined alerts size
     *
     * @param context
     * @param noOfAlarms The number of alarms to cancel
     */
    public void cancelAlarm(Context context, int noOfAlarms) {
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        for (int i = 0; i < noOfAlarms; i++) {
            Intent intent = new Intent(context, CheckInAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            alarmIntent.cancel();
            alarmMgr.cancel(alarmIntent);
        }
    }
}
