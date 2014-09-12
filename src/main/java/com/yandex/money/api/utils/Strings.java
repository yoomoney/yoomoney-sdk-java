package com.yandex.money.api.utils;

/**
 * Common strings operations.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class Strings {

    private Strings() {
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
}
