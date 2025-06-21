/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
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
