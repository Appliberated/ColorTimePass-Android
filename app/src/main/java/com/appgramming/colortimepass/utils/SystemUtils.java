/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */
package com.appgramming.colortimepass.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.appgramming.colortimepass.R;

/**
 * System utility methods.
 */
public class SystemUtils {

    /**
     * Copies a text to the clipboard.
     */
    public static void copyToClipboard(Context context, CharSequence label, CharSequence text) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            final ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * Starts an activity to view an url.
     */
    public static void viewUrl(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.feedback_error_url, url), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Shortcut method to retrieve a boolean value from the preferences.
     */
    public static boolean getBoolPref(Context context, SharedPreferences sharedPref, int keyId, int defaultId) {
        return sharedPref.getBoolean(context.getString(keyId), context.getResources().getBoolean(defaultId));
    }
}
