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

import org.testng.annotations.Test;

import java.text.SimpleDateFormat;

import static org.testng.Assert.assertEquals;

public class YearMonthTest {

    @Test
    public void testParsing() {
        for (int i = 0; i < 4; ++i) {
            int year = i * 10 + 1;
            int month = 1;
            for (int j = 1; j <= 12; ++j) {
                testParsing(year++, month++);
            }
        }
    }

    @Test
    public void testFormatting() {
        for (int i = 0; i < 4; ++i) {
            int year = i * 10 + 1;
            int month = 1;
            for (int j = 1; j <= 12; ++j) {
                testFormatting(year++, month++);
            }
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalYear() {
        new YearMonth(-1, 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalMonth() {
        new YearMonth(2000, 13);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalMonthNegative() {
        new YearMonth(2000, -1);
    }

    private void testParsing(int year, int month) {
        assertEquals(YearMonth.parse(String.format("%04d-%02d", year, month)), new YearMonth(year, month));
    }

    private void testFormatting(int year, int month) {
        assertEquals(new YearMonth(year, month).toString(), String.format("%04d-%02d", year, month));
        assertEquals(new YearMonth(year, month).toString(new SimpleDateFormat("MM/yy")),
                String.format("%02d/%02d", month, year));
    }
}
