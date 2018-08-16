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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
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

        DateTime dateTime = DateTime.from(year, month, dayOfMonth, hourOfDay, minute);
        assertNotNull(dateTime);
        assertEquals(dateTime.getYear(), year);
        assertEquals(dateTime.getMonth(), month);
        assertEquals(dateTime.getDayOfMonth(), dayOfMonth);
        assertEquals(dateTime.getHourOfDay(), hourOfDay);
        assertEquals(dateTime.getMinute(), minute);
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

    @Test
    public void testWithTimeAtStartOfDay() {
        DateTime now = DateTime.from(System.currentTimeMillis(), TimeZone.getTimeZone("GMT"));
        DateTime withTimeAtStartOfDay = now.withTimeAtStartOfDay();
        assertNotEquals(withTimeAtStartOfDay, now);

        long mod = TimeUnit.HOURS.toMillis(24);
        long millis = withTimeAtStartOfDay.getDate().getTime();
        assertEquals(millis % mod, 0);
    }

    @Test
    public void testWithTimeZone() throws ParseException {
        DateTime dateTime = Iso8601Format.parse("1970-01-01T00:00:00.000Z");

        dateTime = dateTime.withZone(TimeZone.getTimeZone("GMT+04:00"));
        assertEquals(dateTime.getYear(), 1970);
        assertEquals(dateTime.getMonth(), Calendar.JANUARY);
        assertEquals(dateTime.getDayOfMonth(), 1);
        assertEquals(dateTime.getHourOfDay(), 4);

        dateTime = dateTime.withZone(TimeZone.getTimeZone("GMT-04:00"));
        assertEquals(dateTime.getYear(), 1969);
        assertEquals(dateTime.getMonth(), Calendar.DECEMBER);
        assertEquals(dateTime.getDayOfMonth(), 31);
        assertEquals(dateTime.getHourOfDay(), 20);

        dateTime = dateTime.withZone(TimeZone.getTimeZone("GMT"));
        assertEquals(dateTime.getYear(), 1970);
        assertEquals(dateTime.getMonth(), Calendar.JANUARY);
        assertEquals(dateTime.getDayOfMonth(), 1);
        assertEquals(dateTime.getHourOfDay(), 0);
    }

    @Test
    public void tesISO8601() throws ParseException {
        DateTime year = Iso8601Format.parse("2018");
        assertEquals(year.getYear(), 2018);

        DateTime yearMonth = Iso8601Format.parse("2018-05");
        assertEquals(yearMonth.getYear(), 2018);
        assertEquals(yearMonth.getMonthOfYear(), 5);

        DateTime yearMonthDay = Iso8601Format.parse("2018-05-06");
        assertEquals(yearMonthDay.getYear(), 2018);
        assertEquals(yearMonthDay.getMonthOfYear(), 5);
        assertEquals(yearMonthDay.getDayOfMonth(), 6);

        DateTime yearMonthDayTime = Iso8601Format.parse("2018-05-06T06:06:06.000Z");
        assertEquals(yearMonthDayTime.getYear(), 2018);
        assertEquals(yearMonthDayTime.getMonthOfYear(), 5);
        assertEquals(yearMonthDayTime.getDayOfMonth(), 6);
        assertEquals(yearMonthDayTime.getHourOfDay(), 6);
        assertEquals(yearMonthDayTime.getMinute(), 6);
        assertEquals(yearMonthDayTime.getSecond(), 6);
    }

    private DateTime createDateTime() {
        return DateTime.from(1995, Calendar.DECEMBER, 31, 23, 59);
    }
}
