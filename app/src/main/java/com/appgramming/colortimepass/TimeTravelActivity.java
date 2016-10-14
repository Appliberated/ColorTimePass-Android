/*
 * Color Time Pass
 * Copyright (C) 2016 Appgramming. All rights reserved.
 * http://www.appgramming.com
 */
package com.appgramming.colortimepass;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appgramming.colortimepass.helpers.TrueColorTime;

import java.util.concurrent.TimeUnit;

/**
 * The Time Travel Activity class.
 */
public class TimeTravelActivity extends PreferenceActivity {

    private static final String EXTRA_DATE = "com.appgramming.colortimepass.extra.DATE";

    /**
     * Called when the activity is starting. Load the preferences from the XML resource.
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.time_travel);

        // Handle a datetime entered by the user in the "Go back and forth in time" preference
        EditTextPreference gotoPreference = (EditTextPreference) findPreference(getString(R.string.pref_time_travel_goto_key));
        gotoPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                long diffSeconds = TrueColorTime.getTimeTravelDiffSeconds((String) newValue);
                if (diffSeconds == 0) {
                    // Invalid datetime
                    Toast.makeText(TimeTravelActivity.this, R.string.feedback_error_date, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    goTimeTravel(diffSeconds);
                    return true;
                }
            }
        });
    }

    /**
     * Inflate the menu items for use in the action bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_time_travel, menu);
        return true;
    }

    /**
     * Action Bar -> Back to Now
     * Travels back to the present time.
     */
    @SuppressWarnings("UnusedParameters")
    public void actionNow(MenuItem item) {
        goTimeTravel(0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        // Is this a Sample destination preference?
        Bundle extras = preference.getExtras();
        if (extras.containsKey(EXTRA_DATE)) {
            String dateValue = extras.getString(EXTRA_DATE);

            long diffSeconds = TrueColorTime.getTimeTravelDiffSeconds(dateValue);
            goTimeTravel(diffSeconds);
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    /**
     * Prepares for a time travel by writing the Diff Seconds value to the preferences, finishing the Time Travel
     * activity, and returning to the main activity. The main activity will read the Diff Seconds value, and...
     * time travel.
     */
    @SuppressWarnings("deprecation")
    private void goTimeTravel(long diffSeconds) {

        // Do we have to arrive 1 minute earlier?
        SwitchPreference earlierPreference = (SwitchPreference) findPreference(getString(R.string.pref_earlier_key));
        long earlierSeconds = earlierPreference.isChecked() ? TimeUnit.MINUTES.toSeconds(1) : 0;
        diffSeconds -= earlierSeconds;

        // Save the time travel Diff Seconds to preferences
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putLong(getString(R.string.pref_time_travel_diff_seconds_key), diffSeconds);
        editor.apply();

        // Finish the Time Travel activity
        finish();
    }
}