/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */
package com.appgramming.colortimepass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appgramming.colortimepass.helpers.TrueColorTime;
import com.appgramming.colortimepass.utils.ColorUtils;
import com.appgramming.colortimepass.utils.SystemUtils;
import com.appgramming.colortimepass.utils.UiUtils;
import com.appgramming.colortimepass.utils.Utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ColorTimePassActivity extends Activity implements View.OnLongClickListener {

//region Fields

    // The Handler to use to update the color and the visible info text views every second
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    // Main views
    private ViewGroup mRootLayout;
    private ViewGroup mClockLayout;
    private ProgressBar mYearProgressBar;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private TextView mUnixTimeTextView;
    private TextView mColorTextView;
    private TextView mDetailsTextView;

    // What info views to show and other flags
    private boolean mDetailsScreen = false;
    private boolean mShowDate = true;
    private boolean mShowTime = true;
    private boolean mShowUnixTime = true;
    private boolean mShowColor = true;

    // Cached date, time and number format instances
    private DateFormat mDateInstance;
    private DateFormat mTimeInstance;
    private NumberFormat mIntegerInstance;

    // Update the color and the visible info text views every second
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeInfo();

            // Run every second (based on
            // http://androidxref.com/7.0.0_r1/xref/frameworks/base/core/java/android/widget/TextClock.java#175)
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            mHandler.postAtTime(mUpdateRunnable, next);
        }
    };

//endregion

//region Activity Initialization and Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_time_pass);

        // Find and init main views
        mRootLayout = (ViewGroup) findViewById(R.id.layout_root);
        mClockLayout = (ViewGroup) findViewById(R.id.layout_clock);
        mYearProgressBar = (ProgressBar) findViewById(R.id.progress_year);
        mDateTextView = initInfoTextView(R.id.text_date);
        mTimeTextView = initInfoTextView(R.id.text_time);
        mUnixTimeTextView = initInfoTextView(R.id.text_unix_time);
        mColorTextView = initInfoTextView(R.id.text_color);
        mDetailsTextView = initInfoTextView(R.id.text_details);

        // Set the default preferences, and load first preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        loadFirstPreferences();

        // Cache date, time and number format instances
        cacheFormatInstances();

        // Prepare for full screen exit
        prepareForFullScreenExit();

        // Fix the progress bar color on older Android versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            UiUtils.setProgressBarColor(mYearProgressBar, ColorUtils.getColor(this, R.color.color_accent));
        }

        // Always show the overflow menu even if the phone has a menu button
        UiUtils.setOverflowMenuAlwaysOn(this);
    }

    /**
     * Loads preferences and starts updating time when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Cache date, time and number format instances
        cacheFormatInstances();

        // Load current preferences, as they may have been changed by the Settings activity
        loadPreferences();

        // Update the current true color year progress bar (do not update it every second because it would take a few
        // hours to see a pixel change in the progress bar - what activity can survive that long without resuming?)
        mYearProgressBar.setProgress(TrueColorTime.getSecondsThisYear());

        // Start the update runnable
        mUpdateRunnable.run();
    }

    /**
     * Stop updating time and save app preferences when the activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mUpdateRunnable);
        savePreferences();
    }

    /**
     * Inflate the menu items for use in the action bar, and update and disable some required menu items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_color_time_pass, menu);

        // Toggle the Details screen and menu
        MenuItem item = menu.findItem(R.id.action_toggle_details);
        if (mDetailsScreen) actionToggleDetails(item);

        // The fullscreen feature is not available on Android versions older than KitKat
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            menu.findItem(R.id.action_fullscreen).setVisible(false);
        }
        return true;
    }

    /**
     * Adds a listener to unblock the screen orientation and show the year progress bar when the user exits fullscreen.
     */
    private void prepareForFullScreenExit() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(final int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    mYearProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Initializes an info text view.
     */
    private TextView initInfoTextView(int id) {
        TextView textView = (TextView) findViewById(id);
        textView.setOnLongClickListener(this);
        return textView;
    }

    /*
     * Cache date, time and number format instances.
     */
    private void cacheFormatInstances() {
        mDateInstance = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        mTimeInstance = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);
        mIntegerInstance = NumberFormat.getIntegerInstance(Locale.US);
    }

//endregion

//region Main Info View Functionality

    private void updateTimeInfo() {

        // The main functionality: calculate the current time millis, the current unix second, and the current date
        long currentMillis = TrueColorTime.currentTimeMillis();
        long unixSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);
        Date date = new Date(currentMillis);

        // Calculate the color of the current second and the contrast color for the text
        int color = TrueColorTime.getColor(unixSeconds);
        int contrastColor = ColorUtils.getContrastColor(color);

        // Fill the screen and system bars with the color of the current second
        mRootLayout.setBackgroundColor(color);
        UiUtils.setSystemBarsColor(getWindow(), color, getResources().getBoolean(R.bool.navigation_bar_on_right));

        // Update the info text views
        if (mDetailsTextView.getVisibility() == View.VISIBLE) {
            String details = TrueColorTime.formatTrueColorYearDetails(this, mDateInstance, mTimeInstance);
            mDetailsTextView.setText(Utils.fromHtml(details));
            mDetailsTextView.setTextColor(contrastColor);
        } else {
            if (mShowDate) {
                mDateTextView.setText(mDateInstance.format(date));
                mDateTextView.setTextColor(contrastColor);
            }
            if (mShowTime) {
                mTimeTextView.setText(mTimeInstance.format(date));
                mTimeTextView.setTextColor(contrastColor);
            }
            if (mShowUnixTime) {
                mUnixTimeTextView.setText(mIntegerInstance.format(unixSeconds));
                mUnixTimeTextView.setTextColor(contrastColor);
            }
            if (mShowColor) {
                mColorTextView.setText(ColorUtils.getColorNameOrCode(color));
                mColorTextView.setTextColor(contrastColor);
            }
        }
    }

    /**
     * Copies the contents to the clipboard when an info view is long clicked.
     */
    @Override
    public boolean onLongClick(View v) {
        // Copy the info from the text view to the clipboard
        CharSequence info = ((TextView) v).getText();
        SystemUtils.copyToClipboard(this, getString(R.string.copy_label), info);

        // Show the "copied to clipboard" feedback toast
        if (v == mDetailsTextView) info = getString(R.string.copy_info_replacement);
        Toast.makeText(this, getString(R.string.feedback_copied, info), Toast.LENGTH_SHORT).show();
        return true;
    }

//endregion

//region Action Bar Items

    /**
     * Action Bar -> Toggle Details Mode
     * Enters or exits the true color year details mode.
     */
    public void actionToggleDetails(MenuItem item) {
        if (mDetailsTextView.getVisibility() == View.VISIBLE) {
            mDetailsTextView.setVisibility(View.GONE);
            mClockLayout.setVisibility(View.VISIBLE);
            item.setIcon(R.drawable.ic_timelapse_black_24dp);
        } else {
            mClockLayout.setVisibility(View.GONE);
            mDetailsTextView.setVisibility(View.VISIBLE);
            item.setIcon(R.drawable.ic_access_time_black_24dp);
        }
        updateTimeInfo();
    }

    /**
     * Action Bar -> Full screen
     * Enter the full screen mode (not available on Android versions older than KitKat).
     */
    @SuppressWarnings("UnusedParameters")
    public void actionFullScreen(MenuItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mYearProgressBar.setVisibility(View.GONE);
            UiUtils.setImmersiveFullScreen(getWindow());

            // Prevent activity rotation and recreation in full screen mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }
    }

    /**
     * Action Bar -> Time travel
     * Starts the Time Travel activity.
     */
    @SuppressWarnings("UnusedParameters")
    public void actionTimeTravel(MenuItem item) {
        startActivity(new Intent(this, TimeTravelActivity.class));
    }

    /**
     * Action Bar -> Settings
     * Starts the Settings activity.
     */
    @SuppressWarnings("UnusedParameters")
    public void actionSettings(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * Action Bar -> Rate app
     * Opens the app Google Play page to allow the user to rate the app.
     */
    @SuppressWarnings("UnusedParameters")
    public void actionRate(MenuItem item) {
        SystemUtils.viewUrl(this, getString(R.string.action_rate_url));
    }

    /**
     * Action Bar -> Help
     * Opens the app home page in the default browser.
     */
    @SuppressWarnings("UnusedParameters")
    public void actionHelp(MenuItem item) {
        SystemUtils.viewUrl(this, getString(R.string.action_help_url));
    }


//endregion

//region Preferences

    /**
     * Load the preferences required by onCreate from the Shared Preferences.
     */
    private void loadFirstPreferences() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        mDetailsScreen = SystemUtils.getBoolPref(this, pref, R.string.pref_details_key, R.bool.pref_details_default);
    }

    /**
     * Load the preferences required by onResume from the Shared Preferences.
     */
    private void loadPreferences() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        // Load and apply "show" preferences
        mShowDate = SystemUtils.getBoolPref(this, pref, R.string.pref_show_date_key, R.bool.pref_show_date_default);
        mDateTextView.setVisibility(mShowDate ? View.VISIBLE : View.GONE);
        mShowTime = SystemUtils.getBoolPref(this, pref, R.string.pref_show_time_key, R.bool.pref_show_time_default);
        mTimeTextView.setVisibility(mShowTime ? View.VISIBLE : View.GONE);
        mShowUnixTime = SystemUtils.getBoolPref(this, pref, R.string.pref_show_unix_time_key, R.bool.pref_show_unix_time_default);
        mUnixTimeTextView.setVisibility(mShowUnixTime ? View.VISIBLE : View.GONE);
        mShowColor = SystemUtils.getBoolPref(this, pref, R.string.pref_show_color_key, R.bool.pref_show_color_default);
        mColorTextView.setVisibility(mShowColor ? View.VISIBLE : View.GONE);

        // Load and apply the active time travel difference in seconds
        long diffSeconds = pref.getLong(getString(R.string.pref_time_travel_diff_seconds_key), 0);
        TrueColorTime.setDiffSeconds(diffSeconds);
    }

    /**
     * Save preferences.
     */
    private void savePreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(getString(R.string.pref_details_key), mDetailsTextView.getVisibility() == View.VISIBLE)
                .apply();
    }

//endregion
}