package org.coursera.capstone.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import org.coursera.capstone.android.R;

import java.util.Calendar;

/**
 * Preference to set time.
 */
public class TimePreference extends DialogPreference {
    private String time;
    private TimePicker picker;

    public TimePreference(Context ctx, String initial) {
        this(ctx, null, initial);
    }

    public TimePreference(Context ctx, AttributeSet attrs, String initial) {
        this(ctx, attrs, 0, initial);
    }

    public TimePreference(Context ctx, AttributeSet attrs, int defStyle, String initial) {
        super(ctx, attrs, defStyle);

        setPositiveButtonText(R.string.timepicker_ok_btn);
        setNegativeButtonText(R.string.timepicker_cancel_btn);
        time = getPersistedString(initial);
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return picker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        String[] hourMinute = time.split(":");
        picker.setCurrentHour(Integer.parseInt(hourMinute[0]));
        picker.setCurrentMinute(Integer.parseInt(hourMinute[1]));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {

            time = niceTime(picker.getCurrentHour()) + ":" + niceTime(picker.getCurrentMinute());
            if (callChangeListener(time)) {
                persistString(time);
                notifyChanged();
            }
        }
    }

    @Override
    public CharSequence getSummary() {
        // Get in format based on user locale
        String[] hourMinute = time.split(":");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinute[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(hourMinute[1]));
        return DateFormat.getTimeFormat(getContext()).format(c.getTime());
    }

    public String getTimeValue() {
        return time;
    }

    private String niceTime(int hourMinute) {
        return hourMinute >= 0 && hourMinute < 10 ? "0" + hourMinute : String.valueOf(hourMinute);
    }
}
