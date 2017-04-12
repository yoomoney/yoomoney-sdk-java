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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class IntervalTest {

    @Test
    public void testFromDates() {
        DateTime from = DateTime.now();
        DateTime till = DateTime.from(from.getDate().getTime() + 1234567890L);
        Interval interval = new Interval(from, till);
        assertNotNull(interval.from);
        assertNotNull(interval.till);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromDatesIllegalArgumentException() {
        DateTime from = DateTime.now();
        DateTime till = DateTime.from(from.getDate().getTime() - 1234567890L);
        new Interval(from, till);
    }

    @Test
    public void testRightInterval() {
        DateTime from = DateTime.now();
        Interval interval = new Interval(from, Days.from(1));
        assertNotNull(interval.from);
        assertNotNull(interval.till);
    }

    @Test
    public void testLeftInterval() {
        DateTime till = DateTime.now();
        Interval interval = new Interval(Days.from(1), till);
        assertNotNull(interval.from);
        assertNotNull(interval.till);
    }

    @Test
    public void testContains() {
        DateTime from = DateTime.from(1995, Calendar.JANUARY, 1, 0, 0);
        DateTime till = DateTime.from(1996, Calendar.JANUARY, 1, 0, 0);
        Interval interval = new Interval(from, till);

        assertFalse(interval.contains(DateTime.from(1994, Calendar.DECEMBER, 31, 23, 59, 59)));
        assertTrue(interval.contains(DateTime.from(1995, Calendar.JANUARY, 1, 0, 0)));
        assertTrue(interval.contains(DateTime.from(1995, Calendar.JULY, 1, 0, 0)));
        assertTrue(interval.contains(DateTime.from(1995, Calendar.DECEMBER, 31, 23, 59, 59)));
        assertFalse(interval.contains(DateTime.from(1996, Calendar.JANUARY, 1, 0, 0)));
        assertFalse(interval.contains(DateTime.from(1996, Calendar.JANUARY, 1, 0, 0, 1)));
    }
}
