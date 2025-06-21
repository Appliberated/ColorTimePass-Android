/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */
package com.appgramming.colortimepass.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.appgramming.colortimepass.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility methods for parsing and formatting dates.
 */
public class DateUtils {

    // Opinionated date formats to parse dates
    private static final SimpleDateFormat[] DATE_FORMATS = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd", Locale.US),
    };

    /**
     * Return a duration formatted string.
     * (Based on http://stackoverflow.com/a/9027379/220039)
     *
     * @param context        A context for accessing string resources.
     * @param recycle        {@link StringBuilder} to recycle, or null to use a temporary one.
     * @param elapsedSeconds The duration to format in seconds.
     */
    @SuppressWarnings("SameParameterValue")
    public static CharSequence formatShortFixedDuration(Context context, StringBuilder recycle, long elapsedSeconds) {
        final Resources res = context.getResources();

        // Create a StringBuilder if we weren't given one to recycle
        StringBuilder sb = recycle;
        if (sb == null) {
            sb = new StringBuilder(16);
        } else {
            sb.setLength(0);
        }

        // Add days
        final long days = TimeUnit.SECONDS.toDays(elapsedSeconds);
        sb.append(res.getString(R.string.duration_day, days));

        // Add hours
        final long hours = TimeUnit.SECONDS.toHours(elapsedSeconds) % TimeUnit.DAYS.toHours(1L);
        sb.append(res.getString(R.string.duration_hour, hours));

        // Add minutes
        final long minutes = TimeUnit.SECONDS.toMinutes(elapsedSeconds) % TimeUnit.HOURS.toMinutes(1L);
        sb.append(res.getString(R.string.duration_minute, minutes));

        // Add seconds
        final long seconds = elapsedSeconds % TimeUnit.MINUTES.toSeconds(1L);
        sb.append(res.getString(R.string.duration_second, seconds));

        return sb.toString();
    }

    /**
     * Parses the date value using some (opinionated) date formats.
     *
     * @return A date object if the string is successfully parsed, null otherwise.
     * @see <a href="https://goo.gl/FRBkXG">com.android.contacts.common.util.DateUtils.parseDate</a><br />
     * <a href="https://goo.gl/di7f4T">org.apache.http.impl.cookie.DateUtils.parseDate</a>
     */
    public static Date parseDate(String dateValue) {

        if (TextUtils.isEmpty(dateValue)) return null;

        for (SimpleDateFormat dateParser : DATE_FORMATS) {
            try {
                return dateParser.parse(dateValue);
            } catch (ParseException pe) {
                // ignore this exception, we will try the next format
            }
        }

        return null;
    }
}
