/*
 * Color Time Pass
 * Copyright (C) 2016 Appgramming. All rights reserved.
 * http://www.appgramming.com
 */
package com.appgramming.colortimepass;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * The Settings Activity class.
 */
public class SettingsActivity extends PreferenceActivity {

    /**
     * Called when the activity is starting. Load the preferences from the XML resource.
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add standard preferences
        addPreferencesFromResource(R.xml.preferences);
    }
}
