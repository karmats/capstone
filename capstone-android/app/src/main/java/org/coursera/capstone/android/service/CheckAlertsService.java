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
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.Patient;

import java.util.ArrayList;
import java.util.List;

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
        if (intent != null) {
            mApi = SymptomManagementApiBuilder.newInstance(intent.getStringExtra(ACCESS_TOKEN_PARAM));
            final ArrayList<Patient> patients = intent.getParcelableArrayListExtra(PATIENTS_PARAM);
            Log.i(CapstoneConstants.LOG_TAG, "Starting service with access token " + intent.getStringExtra(ACCESS_TOKEN_PARAM));
            // Check if patients has any alerts, if so send a notification
            for (Patient p : patients) {
                List<String> patientAlerts = fetchPatientAlerts(p.getUsername());
                if (!patientAlerts.isEmpty()) {
                    notifyAlert(p, patientAlerts);
                }
            }
        }
    }

    /**
     * Get the patient alerts from provided background thread
     */
    private List<String> fetchPatientAlerts(String username) {
        return mApi.getPatientAlerts(username);
    }

    // Post a notification indicating whether alerts were found.
    private void notifyAlert(Patient p, List<String> alerts) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DoctorMainActivity.class), 0);

        String alertString = "";
        for (String alert : alerts) {
            alertString += alert + "\n";
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(p.getFirstName() + " " + p.getLastName())
                        .setContentText(alertString);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
