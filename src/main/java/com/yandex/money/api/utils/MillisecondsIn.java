package com.yandex.money.api.utils;

/**
 * Convenience class for to get time in milliseconds.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class MillisecondsIn {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
    public static final long MONTH = 30 * DAY;
    public static final long YEAR = 365 * DAY;

    private MillisecondsIn() {
        // prevents instantiating of this class
    }
}
