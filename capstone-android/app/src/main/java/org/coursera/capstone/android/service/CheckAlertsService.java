package org.coursera.capstone.android.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.activity.DoctorMainActivity;
import org.coursera.capstone.android.alarm.CheckAlertsReceiver;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.Alert;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class CheckAlertsService extends IntentService {
    // Parameter for patients to check alerts for
    public static final String PATIENTS_PARAM = "org.coursera.capstone.android.service.extra.PATIENTS_PARAM";
    public static final String ACCESS_TOKEN_PARAM = "org.coursera.capstone.android.service.extra.ACCESS_TOKEN_PARAM";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    private SymptomManagementApi mApi;

    public CheckAlertsService() {
        super("CheckAlertsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(CapstoneConstants.LOG_TAG, "Handling intent " + intent.getAction());
        if (intent != null) {
            mApi = SymptomManagementApiBuilder.newInstance(intent.getStringExtra(ACCESS_TOKEN_PARAM));
            final ArrayList<String> patientUserNames = intent.getStringArrayListExtra(PATIENTS_PARAM);
            // Check if patients has any alerts, if so send a notification
            for (String p : patientUserNames) {
                Alert patientAlerts = fetchPatientAlerts(p);
                if (!patientAlerts.getAlerts().isEmpty()) {
                    notifyAlert(patientAlerts);
                }
            }
            // Release the wake lock provided by the BroadcastReceiver.
            CheckAlertsReceiver.completeWakefulIntent(intent);
        }

    }

    /**
     * Get the patient alerts from provided background thread
     */
    private Alert fetchPatientAlerts(String username) {
        return mApi.getPatientAlerts(username);
    }

    // Post a notification indicating whether alerts were found.
    private void notifyAlert(Alert alert) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DoctorMainActivity.class), 0);

        String alertString = "";
        for (String a : alert.getAlerts()) {
            if (!alertString.contains(a)) {
                alertString += a + "\n";
            }

        }
        Log.i(CapstoneConstants.LOG_TAG, alertString);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(alert.getFirstName() + " " + alert.getLastName())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(alertString))
                        .setContentText(alertString);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
