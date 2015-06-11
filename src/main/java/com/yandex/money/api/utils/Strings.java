/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.utils;

/**
 * Common strings operations.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class Strings {

    private Strings() {
        // prevents instantiating of this class
    }

    /**
     * Checks if value is null or empty.
     *
     * @param value value
     * @return {@code true} if null or empty
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Checks if string value contains digits only.
     *
     * @param value value
     * @return {@code true} if digits only
     */
    public static boolean containsDigitsOnly(String value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return value.matches("\\d*");
    }

    /**
     * Concatenates {@code array} of strings to one string using {@code splitter} as a separator.
     *
     * @param array array of strings
     * @param splitter separator
     * @return concatenated string
     */
    public static String concatenate(String[] array, String splitter) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        if (splitter == null) {
            throw new NullPointerException("splitter is null");
        }
        if (array.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(array[0]);
        for (int i = 1; i < array.length; ++i) {
            sb.append(splitter).append(array[i]);
        }
        return sb.toString();
    }

    /**
     * Splits {@code str} to array of strings where the max length of each string is equals or less
     * than {@code n}.
     *
     * @param str source string
     * @param n max length
     * @return array of strings
     */
    public static String[] split(String str, int n) {
        if (str == null) {
            throw new NullPointerException("str is null");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("n should be greater than 0");
        }

        final int length = str.length();
        String[] result = new String[length / n + (length % n == 0 ? 0 : 1)];
        for (int i = 0; i < result.length; ++i) {
            int beginIndex = i * n;
            int endIndex = (i + 1) * n;
            result[i] = str.substring(beginIndex, endIndex < length ? endIndex : length);
        }
        return result;
    }

    /**
     * Splits string using separator to divide groups.
     *
     * @param str the string to split
     * @param groupSize group size
     * @param separator separator
     * @return grouped string
     */
    public static String group(String str, int groupSize, String separator) {
        String[] split = split(str, groupSize);
        return concatenate(split, separator);
    }

    public static void checkNotNullAndNotEmpty(String value, String field) {
        if (isNullOrEmpty(value)) {
            throw new IllegalArgumentException(field + " is null or empty");
        }
    }
}
