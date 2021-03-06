package org.coursera.capstone.android.constant;

/**
 * App specific constants.
 */
public interface CapstoneConstants {

    /**
     * The log tag that should be used across the application.
     */
    static final String LOG_TAG = "SymptomManagement";

    /**
     * The user key in Shared preferences.
     */
    static final String PREFERENCES_USER = "pref_user";
    // User preference keys
    static final String PREFERENCES_NAME = "pref_name";
    static final String PREFERENCES_DATE_OF_BIRTH = "pref_dob";
    static final String PREFERENCES_MEDICAL_RECORD_NUMBER = "pref_medical_record_no";
    static final String PREFERENCES_PATIENT_DOCTOR_NAME = "pref_patient_doctor_name";
    static final String PREFERENCES_PATIENT_REMINDERS = "pref_patient_reminders";
    static final String PREFERENCES_DOCTOR_PATIENT_USERNAMES = "perf_doctor_patient_usernames";

    /**
     * The oauth2 client
     */
    static final String OAUTH_CLIENT = "mobile";

    /**
     * Doctor role name
     */
    static final String DOCTOR_ROLE = "DOCTOR";

    /**
     * Patient role name
     */
    static final String PATIENT_ROLE = "PATIENT";
}
