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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the year and monthOfYear fields.
 */
public final class YearMonth {

    private static final Pattern PATTERN = Pattern.compile("(\\d{4})-(\\d{2})");

    /**
     * Year.
     */
    @SuppressWarnings("WeakerAccess")
    public final int year;

    /**
     * Month of year.
     */
    public final int month;

    /**
     * Creates an instance of this class.
     *
     * @param year year
     * @param month month of year
     */
    @SuppressWarnings("WeakerAccess")
    public YearMonth(int year, int month) {
        if (year < 0) {
            throw new IllegalArgumentException("negative year: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month of year is out of bounds: " + month);
        }
        this.year = year;
        this.month = month;
    }

    /**
     * Parses year month value if represented by the pattern yyyy-MM
     *
     * @param value string representation of year-month
     * @return year month instance
     */
    public static YearMonth parse(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches() || matcher.groupCount() != 2) {
            throw new IllegalArgumentException("unsupported value: " + value);
        }
        return new YearMonth(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YearMonth yearMonth = (YearMonth) o;

        //noinspection SimplifiableIfStatement
        if (year != yearMonth.year) return false;
        return month == yearMonth.month;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d", year, month);
    }

    public String toString(DateFormat formatter) {
        return DateTime.from(year, month - 1, 1, 0, 0).toString(formatter);
    }
}
