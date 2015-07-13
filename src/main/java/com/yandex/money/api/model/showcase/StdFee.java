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

import com.yandex.money.api.exceptions.IllegalAmountException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Standard fee.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class StdFee implements Fee {

    private static final BigDecimal ABSOLUTE_MINIMUM_AMOUNT = new BigDecimal("0.01");
    private static final MathContext UNLIMITED_MODE = new MathContext(34, RoundingMode.HALF_UP);

    /**
     * Coefficient of amount due.
     */
    public final BigDecimal a;
    /**
     * Fixed amount for a single transaction.
     */
    public final BigDecimal b;
    /**
     * Min fee per transaction.
     */
    public final BigDecimal c;
    /**
     * Max fee per transaction.
     */
    public final BigDecimal d;

    private final AmountType amountType;

    private final BigDecimal revA; // = 1 / (1 + a)
    private final boolean hasCommission;

    public StdFee(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d, AmountType amountType) {
        if (a == null || a.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Coefficient A is null or negative");
        }
        if (b == null || b.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Coefficient B is null or negative");
        }
        if (c == null || c.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Coefficient C is null or negative");
        }
        if (d != null && d.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Coefficient D is negative");
        }
        if (amountType == null) {
            throw new NullPointerException("amountType is null");
        }

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.amountType = amountType;

        hasCommission = !(a.equals(BigDecimal.ZERO) && b.equals(BigDecimal.ZERO) &&
                c.equals(BigDecimal.ZERO));
        revA = hasCommission ?
                BigDecimal.ONE.divide(BigDecimal.ONE.add(this.a), UNLIMITED_MODE) : null;
    }

    @Override
    public boolean hasCommission() {
        return hasCommission;
    }

    @Override
    public boolean isCalculable() {
        return true;
    }

    @Override
    public BigDecimal amount(BigDecimal netAmount) {
        // amount = netAmount + min(max(a * netAmount + b, c), d)
        return hasCommission ?
                netAmount.add(checkRules(netAmount.multiply(a, UNLIMITED_MODE).add(b))) : netAmount;
    }

    @Override
    public BigDecimal netAmount(BigDecimal amount) throws IllegalAmountException {
        // netAmount = amount - min(max(amount * (a / (1 + a)) + b / (1 + a), c), d)
        if (hasCommission) {
            final BigDecimal fee = checkRules(amount.multiply(a.multiply(
                    revA, UNLIMITED_MODE), UNLIMITED_MODE).add(b.multiply(revA)));
            final BigDecimal netAmount = amount.subtract(fee);
            if (netAmount.compareTo(ABSOLUTE_MINIMUM_AMOUNT) >= 0) {
                return netAmount;
            } else {
                throw new IllegalAmountException(fee.add(ABSOLUTE_MINIMUM_AMOUNT));
            }
        } else {
            return amount;
        }
    }

    @Override
    public AmountType getAmountType() {
        return amountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StdFee fee = (StdFee) o;

        return a.equals(fee.a) && b.equals(fee.b) && c.equals(fee.c) &&
                !(d != null ? !d.equals(fee.d) : fee.d != null) && amountType == fee.amountType;

    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 31 * result + c.hashCode();
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + amountType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StdFee{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", amountType=" + amountType +
                '}';
    }

    private BigDecimal checkRules(BigDecimal fee) {
        fee = fee.compareTo(b) < 0 ? b : fee;
        fee = fee.compareTo(c) > 0 ? fee : c;
        if (d != null) {
            fee = fee.compareTo(d) > 0 ? d : fee;
        }
        // rounding fee amount
        fee = fee.setScale(2, RoundingMode.HALF_UP);
        return fee.compareTo(ABSOLUTE_MINIMUM_AMOUNT) > 0 ? fee : ABSOLUTE_MINIMUM_AMOUNT;
    }
}
