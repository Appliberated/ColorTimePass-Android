/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */
package com.appgramming.colortimepass.helpers;

import android.content.Context;

import com.appgramming.colortimepass.R;
import com.appgramming.colortimepass.utils.DateUtils;
import com.appgramming.colortimepass.utils.Utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Helper methods for the true color time concept.
 */

public class TrueColorTime {

    private static final int FULL_ALPHA_MASK = 0xFF000000;
    private static final long TRUE_COLOR_COUNT = 16_777_216L;
    private static long sDiffSeconds;

//region True Color Time

    /**
     * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC, or since the time travel jump.
     */
    public static long currentTimeMillis() {
        if (sDiffSeconds == 0) {
            return System.currentTimeMillis();
        } else {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + sDiffSeconds;
            return TimeUnit.SECONDS.toMillis(seconds);
        }
    }

    /**
     * Sets the number of difference seconds of the current time travel.
     */
    public static void setDiffSeconds(long diffSeconds) {
        sDiffSeconds = diffSeconds;
    }

    /**
     * Returns the current true color year.
     */
    private static int getYear() {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(TrueColorTime.currentTimeMillis());
        seconds = Math.abs(seconds);
        return (int) (seconds / TRUE_COLOR_COUNT) + 1;
    }

    /**
     * Returns true if the current true color year is after the first year of the true color era.
     */
    private static boolean isAfterTrueColorEra() {
        return TrueColorTime.currentTimeMillis() >= 0;
    }

    /**
     * Returns the number of seconds elapsed this true color year.
     */
    public static int getSecondsThisYear() {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(TrueColorTime.currentTimeMillis());
        return (int) (Utils.mod(seconds, TRUE_COLOR_COUNT));
    }

    /**
     * Returns the number of milliseconds remaining this true color year.
     */
    private static long getRemainingMillisThisYear() {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(TrueColorTime.currentTimeMillis());
        return TimeUnit.SECONDS.toMillis(TRUE_COLOR_COUNT - Utils.mod(seconds, TRUE_COLOR_COUNT));
    }

    /**
     * Returns the time in milliseconds of the start of the next true color year.
     */
    private static long getNextYearStartMillis() {
        return TrueColorTime.currentTimeMillis() + TrueColorTime.getRemainingMillisThisYear();
    }

    /**
     * Returns the color of the current Unix time second.
     */
    public static int getColor(long unixTimeSeconds) {
        int color = (int) (unixTimeSeconds % TRUE_COLOR_COUNT);
        return FULL_ALPHA_MASK | color;
    }

//endregion

//region Time Travel

    /**
     * Calculates and returns the magic formula of time travel: destination time millis - current time millis.
     */
    public static long getTimeTravelDiffSeconds(String dateValue) {
        Date date = DateUtils.parseDate(dateValue);
        if (date != null) {
            return TimeUnit.MILLISECONDS.toSeconds(date.getTime() - System.currentTimeMillis());
        }

        return 0;
    }

//endregion

//region Time Formatting

    /**
     * Returns the text that should be displayed in the details screen, with updated time components.
     */
    public static String formatTrueColorYearDetails(Context context, DateFormat dateFormat, DateFormat timeFormat) {
        Date nextYearStartDate = new Date(TrueColorTime.getNextYearStartMillis());
        return String.format(
                context.getString(R.string.text_details),
                TrueColorTime.getYear(),
                context.getString(TrueColorTime.isAfterTrueColorEra() ? R.string.atc_era : R.string.btc_era),
                dateFormat.format(nextYearStartDate),
                timeFormat.format(nextYearStartDate),
                DateUtils.formatShortFixedDuration(context, null,
                        TimeUnit.MILLISECONDS.toSeconds(TrueColorTime.getRemainingMillisThisYear())));
    }

//endregion
}