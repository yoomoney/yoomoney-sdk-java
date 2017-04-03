/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 NBCO Yandex.Money LLC
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

package com.yandex.money.api.time;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Represents a date-time value that is backed up by a {@link Calendar} instance. The implementation of this class is
 * immutable.
 *
 * @see Calendar
 */
public final class DateTime {

    private final Calendar calendar;

    private DateTime(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * @return an instance of this class with current time and default timezone
     * @see Calendar#getInstance()
     */
    public static DateTime now() {
        return new DateTime(Calendar.getInstance());
    }

    /**
     * @param millis the time in UTC milliseconds from the epoch
     * @return an instance of this class with specified time and default timezone
     */
    public static DateTime from(long millis) {
        return from(millis, TimeZone.getDefault());
    }

    /**
     * @param millis the time in UTC milliseconds from the epoch
     * @param timeZone the given time zone
     * @return an instance of this class with specified time and timezone
     */
    public static DateTime from(long millis, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.setTimeZone(timeZone);
        return new DateTime(calendar);
    }

    /**
     * @param date the given date
     * @return an instance of this class with specified date and default timezone
     */
    public static DateTime from(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new DateTime(calendar);
    }

    /**
     * @param year a year
     * @param month a month
     * @param date a date
     * @param hour an hour
     * @param minute a minute
     * @return an instance of this class with specified values and default timezone
     */
    public static DateTime from(int year, int month, int date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return new DateTime(calendar);
    }

    /**
     * @param year a year
     * @param month a month
     * @param date a date
     * @param hour an hour
     * @param minute a minute
     * @param second a second
     * @return an instance of this class with specified values and default timezone
     */
    public static DateTime from(int year, int month, int date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute, second);
        calendar.clear(Calendar.MILLISECOND);
        return new DateTime(calendar);
    }

    /**
     * Adds specific period.
     *
     * @param period a period to add
     * @return modified instance of this class
     */
    public DateTime plus(Period period) {
        return add(period, 1);
    }

    /**
     * Adds specific period.
     *
     * @param period a period to add
     * @return modified instance of this class
     */
    public DateTime plus(SingleFieldPeriod period) {
        return add(period, 1);
    }

    /**
     * Subtracts specific period.
     *
     * @param period a period to subtract
     * @return modified instance of this class
     */
    public DateTime minus(Period period) {
        return add(period, -1);
    }

    /**
     * Subtracts specific period.
     *
     * @param period a period to subtract
     * @return modified instance of this class
     */
    public DateTime minus(SingleFieldPeriod period) {
        return add(period, -1);
    }

    /**
     * @return {@link Date} instance that represents this class
     */
    public Date getDate() {
        return calendar.getTime();
    }

    /**
     * Sets timezone to use by the instance of this class.
     *
     * @param timeZone timezone to use
     * @see Calendar#setTimeZone(TimeZone)
     */
    public void setTimeZone(TimeZone timeZone) {
        calendar.setTimeZone(timeZone);
    }

    /**
     * @return timezone used by the instance of this class
     * @see Calendar#getTimeZone()
     */
    public TimeZone getTimeZone() {
        return calendar.getTimeZone();
    }

    /**
     * Check if current date time comes after specified date time.
     *
     * @param dateTime specified date time
     * @return {@code true} if specified date time comes after current date time, {@code false} otherwise
     * @see Calendar#after(Object)
     */
    public boolean isAfter(DateTime dateTime) {
        return calendar.after(checkNotNull(dateTime, "dateTime").calendar);
    }

    /**
     * Check if current date time comes before specified date time.
     *
     * @param dateTime specified date time
     * @return {@code true} if specified date time comes before current date time, {@code false} otherwise
     * @see Calendar#before(Object)
     */
    public boolean isBefore(DateTime dateTime) {
        return calendar.before(checkNotNull(dateTime, "dateTime").calendar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateTime dateTime = (DateTime) o;

        return calendar.equals(dateTime.calendar);
    }

    @Override
    public int hashCode() {
        return calendar.hashCode();
    }

    @Override
    public String toString() {
        return Iso8061Format.format(this);
    }

    public String toString(DateFormat formatter) {
        return checkNotNull(formatter, "formatter").format(getDate());
    }

    Calendar getCalendar() {
        return calendar;
    }

    private DateTime add(Period period, int multiplier) {
        DateTime result = copy();
        result.calendar.add(Calendar.YEAR, multiplier * period.years);
        result.calendar.add(Calendar.MONTH, multiplier * period.months);
        result.calendar.add(Calendar.DAY_OF_YEAR, multiplier * period.days);
        return result;
    }

    private DateTime add(SingleFieldPeriod period, int multiplier) {
        checkNotNull(period, "period");
        DateTime result = copy();
        //noinspection MagicConstant
        result.calendar.add(period.getField(), multiplier * period.getAmount());
        return result;
    }

    private DateTime copy() {
        return new DateTime((Calendar) calendar.clone());
    }
}
