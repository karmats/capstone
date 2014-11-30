package org.coursera.capstone.android.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.coursera.capstone.android.R;

/**
 * Utility class
 */
public class UIUtils {

    // Should not be possible to initiate class. Only static methods should be used
    private UIUtils() {
    }

    /**
     * Creates two vertical columns text.
     *
     * @param context
     * @param text1    "Title"
     * @param text2    "Value"
     * @param textSize The text size in TypedValue.COMPLEX_UNIT_SP
     * @return
     */
    public static LinearLayout createTwoColumnText(Context context, String text1, String text2, float textSize) {
        LinearLayout holderView = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        holderView.setLayoutParams(params);
        holderView.setOrientation(LinearLayout.VERTICAL);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        holderView.setPadding(0, padding, 0, 0);

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text1View = new TextView(context);
        text1View.setText(text1);
        text1View.setTypeface(null, Typeface.BOLD);
        text1View.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        text1View.setLayoutParams(textViewParams);
        holderView.addView(text1View);

        TextView text2View = new TextView(context);
        text2View.setText(text2);
        text2View.setLayoutParams(textViewParams);
        text2View.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        holderView.addView(text2View);
        return holderView;
    }
}
