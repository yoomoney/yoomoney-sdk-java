/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
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

package com.yandex.money.api.model.showcase;

import java.math.BigDecimal;

/**
 * No fee.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class NoFee implements Fee {

    private static final NoFee INSTANCE = new NoFee();

    private NoFee() {
    }

    /**
     * @return instance of this class
     */
    public static NoFee getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean hasCommission() {
        return false;
    }

    @Override
    public boolean isCalculable() {
        return true;
    }

    @Override
    public BigDecimal amount(BigDecimal netAmount) {
        return netAmount;
    }

    @Override
    public BigDecimal netAmount(BigDecimal amount) {
        return amount;
    }

    @Override
    public AmountType getAmountType() {
        return AmountType.AMOUNT;
    }

    @Override
    public String toString() {
        return "NO_FEE";
    }
}
