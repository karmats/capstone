package org.coursera.capstone.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parceable.User;
import org.coursera.capstone.android.task.FetchPatientTask;

public class DoctorMainActivity extends Activity {

    private TextView mWelcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        // Get the patient name from shared preferences
        String userJsonString = PreferenceManager.getDefaultSharedPreferences(DoctorMainActivity.this)
                .getString(CapstoneConstants.PREFERENCES_USER, "");
        User user = User.fromJsonString(userJsonString);

        mWelcomeText = (TextView) findViewById(R.id.doctor_welcome_text);
        String welcomeText = getString(R.string.welcome_doctor, user.getFirstName() + " " + user.getLastName(),
                                        user.getAccessToken());
        mWelcomeText.append(welcomeText);mWelcomeText.append("\n\nPatients ftw:\n");
        new FetchPatientTask(DoctorMainActivity.this, mWelcomeText).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
