/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */
package com.appliberated.colortimepass.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import com.appliberated.colortimepass.helpers.NamedColors;

/**
 * Utility methods for working with colors.
 */
public class ColorUtils {

    private static final int NO_ALPHA_MASK = 0xFFFFFF;

    /**
     * Gets the Black or White contrast color for a specified color.
     */
    public static int getContrastColor(int color) {
        // Using the perceived luminance formula: (0.299*red + 0.587*green + 0.114*blue)
        long y = (299L * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 192 ? Color.BLACK : Color.WHITE; // 192 is our opinionated value
    }

    /**
     * Returns a color string made up of the color hex code and the name of the color, if any.
     */
    public static String getColorNameOrCode(int color) {
        color = NO_ALPHA_MASK & color;
        String colorName = NamedColors.get(color);
        return !TextUtils.isEmpty(colorName) ? colorName : String.format("#%06X", color);
    }

    /**
     * Wrapper around Resources.getColor(int id) that was deprecated in API 23. Returns a color integer
     * associated with a particular resource ID.
     *
     * @param context The context to use.
     * @param id      The desired resource identifier, as generated by the aapt tool.
     * @return A single color value in the form 0xAARRGGBB.
     * @throws android.content.res.Resources.NotFoundException if the given ID does not exist.
     * @see android.content.Context#getColor
     * @see android.content.res.Resources#getColor
     * @see <a href="https://gist.github.com/AHelloWorldDev/681e6862e931e03bc92d126b104d537e">GitHub Gist</a>
     */
    @SuppressWarnings({"deprecation", "SameParameterValue"})
    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
