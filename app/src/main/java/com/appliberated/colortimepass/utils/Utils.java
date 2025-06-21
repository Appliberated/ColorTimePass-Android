/*
 * Color Time Pass
 * Copyright (C) 2016-2025 Appliberated. All rights reserved.
 * https://www.appliberated.com/
 *
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */
package com.appliberated.colortimepass.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Various utility methods.
 */
public class Utils {

    /**
     * Implements a proper modulus operation in Java.
     *
     * @see <a href="https://gist.github.com/AHelloWorldDev/5b3090105e69ffc0a0c73a5409004fd5">GitHub Gist</a>
     */
    @SuppressWarnings("SameParameterValue")
    public static long mod(long x, long y) {
        long result = x % y;
        return result < 0 ? result + y : result;
    }

    /**
     * Wrapper around Html.fromHtml(String) that was deprecated in API 24. Returns displayable styled
     * text from the provided HTML string.
     *
     * @see android.text.Html#fromHtml(String, int)
     * @see android.text.Html#fromHtml(String)
     * @see <a href="https://gist.github.com/AHelloWorldDev/110e6bbf8027783969c314ee96deddad">GitHub Gist</a>
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
