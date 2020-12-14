/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.time;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Simple utility class to parse and format ISO 8601 dates.
 */
public final class Iso8601Format {

    private Iso8601Format() {
    }

    /**
     * Parses string of ISO 8601 date.
     *
     * @param date a string to parse
     * @return parsed date time
     * @throws ParseException if parsing is not possible
     */
    public static DateTime parse(String date) throws ParseException {
        return parse(date, TimeZone.getDefault());
    }

    /**
     * Parses string of ISO 8601 date.
     *
     * @param date a string to parse
     * @param defaultTimezone a time zone which will be used if date doesn't contain time zone
     * @return parsed date time
     * @throws ParseException if parsing is not possible
     */
    public static DateTime parse(String date, TimeZone defaultTimezone) throws ParseException {
        int position = 0;

        int year = parseInt(date, position, position += 4);
        if (checkPosition(date, position, '-')) {
            ++position;
        }

        boolean hasMonth = position < date.length();
        if (!hasMonth) {
            return DateTime.from(year, 0, 1, 0, 0, defaultTimezone);
        }

        int monthOfYear = parseInt(date, position, position += 2) - 1;
        if (checkPosition(date, position, '-')) {
            ++position;
        }

        boolean hasDay = position < date.length();
        if (!hasDay) {
            return DateTime.from(year, monthOfYear, 1, 0, 0, defaultTimezone);
        }

        int day = parseInt(date, position, position += 2);

        boolean hasTime = checkPosition(date, position, 'T');
        if (!hasTime) {
            return DateTime.from(year, monthOfYear, day, 0, 0, defaultTimezone);
        }

        int hour = parseInt(date, position += 1, position += 2);
        if (checkPosition(date, position, ':')) {
            ++position;
        }

        int minutes = parseInt(date, position, position += 2);
        if (checkPosition(date, position, ':')) {
            ++position;
        }

        int seconds = 0;
        int milliseconds = 0;
        if (position < date.length() && !isTimezoneIndicator(date, position)) {
            seconds = parseInt(date, position, position += 2);
            if (seconds > 59 && seconds < 63) seconds = 59; // truncate up to 3 leap seconds

            if (checkPosition(date, position, '.')) {
                ++position;
                int endPosition = indexOfNonDigit(date, position + 1); // assume at least one digit
                endPosition = Math.min(position + 3, endPosition);
                milliseconds = parseInt(date, position, endPosition);

                switch (endPosition - position) {
                    case 2:
                        milliseconds *= 10;
                        break;
                    case 1:
                        milliseconds *= 100;
                        break;
                }

                position = endPosition;
            }
        }

        if (position > date.length()) {
            throw new ParseException("no timezone indicator", position);
        }

        TimeZone timeZone = null;
        char timeZoneChar = date.charAt(position);

        if (timeZoneChar == 'Z') {
            timeZone = TimeZone.getTimeZone("GMT");
        } else if (timeZoneChar == '+' || timeZoneChar == '-') {
            String timeZoneOffset = date.substring(position);

            timeZoneOffset = timeZoneOffset.length() >= 5 ? timeZoneOffset : timeZoneOffset + "00";

            if ("+0000".equals(timeZoneOffset) || "+00:00".equals(timeZoneOffset)) {
                timeZone = TimeZone.getTimeZone("GMT");
            } else {
                timeZone = TimeZone.getTimeZone("GMT" + timeZoneOffset);
            }
        }

        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milliseconds);

        return new DateTime(calendar);
    }

    /**
     * Formats date time to ISO 8601 string.
     *
     * @param dateTime date time to format
     * @return formatted string
     */
    public static String format(DateTime dateTime) {
        Calendar calendar = checkNotNull(dateTime, "dateTime").getCalendar();
        return ISO8601Utils.format(calendar.getTime(), true, calendar.getTimeZone());
    }

    private static int parseInt(String value, int begin, int end) throws ParseException {
        int result = 0;
        for (int i = begin; i < end; ++i) {
            char c = value.charAt(i);
            if (Character.isDigit(c)) {
                result = result * 10 + Character.getNumericValue(c);
            } else {
                throw new ParseException("unable to parse int value", begin);
            }
        }
        return result;
    }

    private static boolean checkPosition(String value, int position, char expected) {
        return position < value.length() && value.charAt(position) == expected;
    }

    private static boolean isTimezoneIndicator(String value, int position) {
        char c = value.charAt(position);
        return c == 'Z' || c == '+' || c == '-';
    }

    private static int indexOfNonDigit(String value, int position) {
        for (int i = position; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (!Character.isDigit(c)) {
                return i;
            }
        }
        return value.length();
    }
}
