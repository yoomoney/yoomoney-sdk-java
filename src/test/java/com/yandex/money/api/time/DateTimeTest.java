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

import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DateTimeTest {

    @Test
    public void testNow() {
        DateTime now = DateTime.now();
        assertNotNull(now);
        assertEquals(now.getTimeZone(), TimeZone.getDefault());
    }

    @Test
    public void testFromMillis() {
        long millis = System.currentTimeMillis();
        DateTime dateTime = DateTime.from(millis);
        assertNotNull(dateTime);
        assertEquals(dateTime.getDate().getTime(), millis);
        assertEquals(dateTime.getTimeZone(), TimeZone.getDefault());
    }

    @Test
    public void testFromMillisAndTimeZone() {
        long millis = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(356);
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        DateTime dateTime = DateTime.from(millis, timeZone);
        assertNotNull(dateTime);
        assertEquals(dateTime.getDate().getTime(), millis);
        assertEquals(dateTime.getTimeZone(), timeZone);
    }

    @Test
    public void testFromDate() {
        Date date = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(356));
        DateTime dateTime = DateTime.from(date);
        assertNotNull(dateTime);
        assertEquals(dateTime.getDate().getTime(), date.getTime());
        assertEquals(dateTime.getTimeZone(), TimeZone.getDefault());
    }

    @Test
    public void testFromYearMonthDayHourMinute() {
        int year = 1995;
        int month = Calendar.DECEMBER;
        int dayOfMonth = 31;
        int hourOfDay = 23;
        int minute = 59;

        long millis = 820443540000L;

        DateTime dateTime = DateTime.from(year, month, dayOfMonth, hourOfDay, minute);
        assertNotNull(dateTime);
        assertEquals(dateTime.getDate().getTime(), millis);
        assertEquals(dateTime.getTimeZone(), TimeZone.getDefault());
    }

    @Test
    public void testPlus() {
        DateTime origin = createDateTime();

        DateTime actual = origin.plus(Seconds.ONE);
        DateTime expected = DateTime.from(1995, Calendar.DECEMBER, 31, 23, 59, 1);
        assertEquals(actual, expected);

        actual = origin.plus(Minutes.ONE);
        expected = DateTime.from(1996, Calendar.JANUARY, 1, 0, 0);
        assertEquals(actual, expected);

        actual = origin.plus(Hours.ONE);
        expected = DateTime.from(1996, Calendar.JANUARY, 1, 0, 59);
        assertEquals(actual, expected);

        actual = origin.plus(Days.ONE);
        expected = DateTime.from(1996, Calendar.JANUARY, 1, 23, 59);
        assertEquals(actual, expected);

        actual = origin.plus(Weeks.ONE);
        expected = DateTime.from(1996, Calendar.JANUARY, 7, 23, 59);
        assertEquals(actual, expected);

        actual = origin.plus(Months.ONE);
        expected = DateTime.from(1996, Calendar.JANUARY, 31, 23, 59);
        assertEquals(actual, expected);

        actual = origin.plus(Years.ONE);
        expected = DateTime.from(1996, Calendar.DECEMBER, 31, 23, 59);
        assertEquals(actual, expected);
    }

    @Test
    public void testPlusPeriod() {
        DateTime origin = createDateTime();
        DateTime actual = origin.plus(new Period(3, 12, 1));
        DateTime expected = DateTime.from(2000, Calendar.JANUARY, 1, 23, 59);
        assertEquals(actual, expected);
    }

    @Test
    public void testMinus() {
        DateTime origin = createDateTime();

        DateTime actual = origin.minus(Seconds.ONE);
        DateTime expected = DateTime.from(1995, Calendar.DECEMBER, 31, 23, 58, 59);
        assertEquals(actual, expected);

        actual = origin.minus(Minutes.ONE);
        expected = DateTime.from(1995, Calendar.DECEMBER, 31, 23, 58);
        assertEquals(actual, expected);

        actual = origin.minus(Hours.ONE);
        expected = DateTime.from(1995, Calendar.DECEMBER, 31, 22, 59);
        assertEquals(actual, expected);

        actual = origin.minus(Days.ONE);
        expected = DateTime.from(1995, Calendar.DECEMBER, 30, 23, 59);
        assertEquals(actual, expected);

        actual = origin.minus(Weeks.ONE);
        expected = DateTime.from(1995, Calendar.DECEMBER, 24, 23, 59);
        assertEquals(actual, expected);

        actual = origin.minus(Months.ONE);
        expected = DateTime.from(1995, Calendar.NOVEMBER, 30, 23, 59);
        assertEquals(actual, expected);

        actual = origin.minus(Years.ONE);
        expected = DateTime.from(1994, Calendar.DECEMBER, 31, 23, 59);
        assertEquals(actual, expected);
    }

    @Test
    public void testMinusPeriod() {
        DateTime origin = createDateTime();
        DateTime actual = origin.minus(new Period(5, 11, 30));
        DateTime expected = DateTime.from(1990, Calendar.JANUARY, 1, 23, 59);
        assertEquals(actual, expected);
    }

    private DateTime createDateTime() {
        return DateTime.from(820443540000L);
    }
}
