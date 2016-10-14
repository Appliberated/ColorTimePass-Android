/*
 * Color Time Pass
 * Copyright (C) 2016 Appgramming. All rights reserved.
 * http://www.appgramming.com
 */
package com.appgramming.colortimepass.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ProgressBar;

import java.lang.reflect.Field;

/**
 * User interface utility methods.
 */
public class UiUtils {

    /**
     * Sets the full screen immersive mode (on Android versions starting with Android KitKat).
     */
    public static void setImmersiveFullScreen(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
            window.getDecorView().setSystemUiVisibility(visibility);
        }
    }

    /**
     * Sets status and navigation bars color (on Android versions starting with Android Lollipop).
     */
    public static void setSystemBarsColor(Window window, int color, boolean navigationBarOnRight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
            if (!navigationBarOnRight) {
                window.setNavigationBarColor(color);
            }
        }
    }

    /**
     * Programmatically sets the progress and thumb colors of a seek bar. Uses mutated drawables, method that is no
     * longer necessary in Lollipop or above.
     */
    public static void setProgressBarColor(ProgressBar progressBar, int progressColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            progressBar.getProgressDrawable().mutate().setColorFilter(progressColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * Always show the overflow menu even if the phone has a menu button.
     * <p>
     * http://blog.vogella.com/2013/08/06/android-always-show-the-overflow-menu-even-if-the-phone-as-a-menu/
     */
    public static void setOverflowMenuAlwaysOn(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
}
