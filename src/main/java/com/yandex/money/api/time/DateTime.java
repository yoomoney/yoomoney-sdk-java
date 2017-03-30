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

public class DateTime {

    private final Calendar calendar;

    private DateTime(Calendar calendar) {
        this.calendar = calendar;
    }

    public static DateTime now() {
        return new DateTime(Calendar.getInstance());
    }

    public static DateTime from(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new DateTime(calendar);
    }

    public static DateTime from(int year, int month, int date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(year, month, date, hour, minute);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return new DateTime(calendar);
    }

    public DateTime plus(Period period) {
        return add(period, 1);
    }

    public DateTime plusSeconds(int seconds) {
        DateTime result = new DateTime((Calendar) calendar.clone());
        result.calendar.add(Calendar.SECOND, seconds);
        return result;
    }

    public DateTime minus(Period period) {
        return add(period, -1);
    }

    public Date getDate() {
        return calendar.getTime();
    }

    public void setTimeZone(TimeZone timeZone) {
        calendar.setTimeZone(timeZone);
    }

    public TimeZone getTimeZone() {
        return calendar.getTimeZone();
    }

    public boolean isAfter(DateTime dateTime) {
        return calendar.after(checkNotNull(dateTime, "dateTime").calendar);
    }

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

    private DateTime copy() {
        return new DateTime((Calendar) calendar.clone());
    }
}
