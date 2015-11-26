package com.sperchenko.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by stanislav.perchenko on 11/24/2015.
 */
public class OvalTouchableView extends View {

    /**
     * Defines whether the layout pass has ever been performed
     */
    private boolean wasLaiedOut;

    /**
     * Shows wheather the touchable area is oval or not. This is defined during layout procedure
     */
    private boolean isOval;

    /**
     * Central position of the View
     */
    private double x0, y0;

    /**
     * Doubled large dimension of the oval touchable area
     */
    private double doubleA;

    /**
     * Coordinates of Focal point 1
     */
    private double xF1;
    private double yF1;

    /**
     * Coordinates of Focal point 2
     */
    private double xF2;
    private double yF2;

    public OvalTouchableView(Context context) {
        super(context);
    }

    public OvalTouchableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OvalTouchableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OvalTouchableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void layout(int left, int top, int right, int bot) {

        final int width = right - left;
        final int height= bot - top;
        x0 = (double)width / 2.0;
        y0 = (double)height/ 2.0;
        if (width != height) {
            isOval = true;
            float a;
            float b;
            if (width > height) {
                doubleA = width;
                a = (float) width / 2f;
                b = (float) height / 2f;
            } else {
                doubleA = height;
                a = (float) height / 2f;
                b = (float) width / 2f;
            }
            final double halfC = Math.sqrt(a*a - b*b) / 2.0;

            if (width > height) {
                xF1 = x0 - halfC;
                xF2 = x0 + halfC;
                yF1 = yF2 = y0;
            } else {
                yF1 = y0 - halfC;
                yF2 = y0 + halfC;
                xF1 = xF2 = x0;
            }

        } else {
            isOval = false;
        }

        super.layout(left, top, right, bot);
        wasLaiedOut = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (checkTouchInAllowedArea(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    private boolean checkTouchInAllowedArea(MotionEvent event) {
        if (!wasLaiedOut) return true;
        if (event.getAction() != MotionEvent.ACTION_DOWN) return true;
        final float x = event.getX();
        final float y = event.getY();
        if (!isOval) {
            final double dx = x - x0;
            final double dy = y - y0;
            return Math.sqrt(dx*dx + dy*dy) <= x0; // Both x0 and y0 equals our radius
        } else {
            final double dxF1 = x - xF1;
            final double dyF1 = y - yF1;
            final double dxF2 = x - xF2;
            final double dyF2 = y - yF2;
            return Math.sqrt(dxF1*dxF1 + dyF1*dyF1) + Math.sqrt(dxF2*dxF2 + dyF2*dyF2) <= doubleA;
        }
    }
}
