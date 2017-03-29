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

import static com.yandex.money.api.util.Common.checkNotEmpty;

public final class Period {

    final int years;
    final int months;
    final int days;

    Period(int years, int months, int days) {
        if (years < 0) {
            throw new IllegalArgumentException("years");
        }
        if (months < 0) {
            throw new IllegalArgumentException("months");
        }
        if (days < 0) {
            throw new IllegalArgumentException("days");
        }
        this.years = years;
        this.months = months;
        this.days = days;
    }

    public static Period years(int years) {
        return new Period(years, 0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (years != period.years) return false;
        if (months != period.months) return false;
        return days == period.days;
    }

    @Override
    public int hashCode() {
        int result = years;
        result = 31 * result + months;
        result = 31 * result + days;
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "years=" + years +
                ", months=" + months +
                ", days=" + days +
                '}';
    }

    public static Period parse(String period) {
        char[] chars = checkNotEmpty(period, "period")
                .trim()
                .toUpperCase()
                .toCharArray();

        if (chars[0] != 'P') {
            throw new IllegalArgumentException(period + " is not a period");
        }

        int years = 0;
        int months = 0;
        int days = 0;

        int currentValue = 0;
        for (int i = 1; i < chars.length; ++i) {
            final char c = chars[i];
            if (Character.isDigit(c)) {
                currentValue = currentValue * 10 + Character.getNumericValue(c);
            } else {
                switch (c) {
                    case 'Y':
                        years = currentValue;
                        break;
                    case 'M':
                        months = currentValue;
                        break;
                    case 'D':
                        days = currentValue;
                        break;
                    default:
                        throw new UnsupportedOperationException(period + " is not supported");
                }
                currentValue = 0;
            }
        }

        return new Period(years, months, days);
    }
}
