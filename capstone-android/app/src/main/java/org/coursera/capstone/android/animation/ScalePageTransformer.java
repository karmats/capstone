package org.coursera.capstone.android.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Animation transformer for @link{ViewPager} that zooms out the page a bit when changing slides
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    // Transform animation
    public void transformPage(View view, float position) {
        if (position <= 1) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            // Modify the default slide transition to shrink the page
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
            float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horizontalMargin - verticalMargin / 2);
            } else {
                view.setTranslationX(-horizontalMargin + verticalMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        }
    }
}
