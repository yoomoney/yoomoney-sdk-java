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
}
