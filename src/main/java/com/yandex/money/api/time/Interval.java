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

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Interval between two dates.
 */
public final class Interval {

    public final DateTime from;
    public final DateTime till;

    /**
     * Creates an interval between two days.
     *
     * @param from date from
     * @param till date till
     */
    public Interval(DateTime from, DateTime till) {
        if (checkNotNull(from, "from").isAfter(checkNotNull(till, "till"))) {
            throw new IllegalArgumentException("from is after till");
        }
        this.from = from;
        this.till = till;
    }

    /**
     * Creates an interval starting at specific date and with that covers provided period.
     *
     * @param from starting date
     * @param period length of interval
     */
    public Interval(DateTime from, SingleFieldPeriod period) {
        this.from = checkNotNull(from, "from");
        this.till = from.plus(period);
    }

    /**
     * Creates an interval ending at specific date and with that covers provided period.
     *
     * @param period length of interval
     * @param till ending date
     */
    public Interval(SingleFieldPeriod period, DateTime till) {
        this.till = checkNotNull(till, "till");
        this.from = till.minus(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interval interval = (Interval) o;

        if (!from.equals(interval.from)) return false;
        return till.equals(interval.till);
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + till.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "from=" + from +
                ", till=" + till +
                '}';
    }
}
