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

import java.util.Calendar;

/**
 * Period in hours.
 */
public final class Hours extends BaseSingleFieldPeriod {

    /**
     * One hour.
     */
    public static final Hours ONE = new Hours(1);

    private Hours(int amount) {
        super(amount);
    }

    /**
     * Creates an instance of this class.
     *
     * @param amount number of hours
     * @return days period
     */
    public static Hours from(int amount) {
        switch (amount) {
            case 1:
                return ONE;
            default:
                return new Hours(amount);
        }
    }

    @Override
    public int getField() {
        return Calendar.HOUR_OF_DAY;
    }
}
