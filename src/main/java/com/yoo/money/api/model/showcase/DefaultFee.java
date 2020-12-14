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

package com.yoo.money.api.model.showcase;

import com.google.gson.annotations.SerializedName;
import com.yoo.money.api.exceptions.IllegalAmountException;

import java.math.BigDecimal;

import static com.yoo.money.api.util.Common.checkNotNull;

public final class DefaultFee implements Fee {

    @SerializedName("type")
    public final Fee.Type type;
    /**
     * Coefficient of amount due.
     */
    @SerializedName("a")
    public final BigDecimal a;
    /**
     * Fixed amount for a single transaction.
     */
    @SerializedName("b")
    public final BigDecimal b;
    /**
     * Min fee per transaction.
     */
    @SerializedName("c")
    public final BigDecimal c;
    /**
     * Max fee per transaction.
     */
    @SerializedName("d")
    public final BigDecimal d;

    @SerializedName("amount_type")
    public final AmountType amountType;

    private Fee feeDelegate;

    public DefaultFee(Type type, BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d, AmountType amountType) {
        this.type = checkNotNull(type, "type");
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.amountType = amountType;
    }

    @Override
    public boolean hasCommission() {
        return getFeeDelegate().hasCommission();
    }

    @Override
    public boolean isCalculable() {
        return getFeeDelegate().isCalculable();
    }

    @Override
    public BigDecimal amount(BigDecimal netAmount) {
        return getFeeDelegate().amount(netAmount);
    }

    @Override
    public BigDecimal netAmount(BigDecimal amount) throws IllegalAmountException {
        return getFeeDelegate().netAmount(amount);
    }

    @Override
    public AmountType getAmountType() {
        return getFeeDelegate().getAmountType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultFee that = (DefaultFee) o;

        if (type != that.type) return false;
        if (a != null ? !a.equals(that.a) : that.a != null) return false;
        if (b != null ? !b.equals(that.b) : that.b != null) return false;
        if (c != null ? !c.equals(that.c) : that.c != null) return false;
        if (d != null ? !d.equals(that.d) : that.d != null) return false;
        return amountType == that.amountType;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (a != null ? a.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + (amountType != null ? amountType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultFee{" +
                "type=" + type +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", amountType=" + amountType +
                '}';
    }

    private Fee getFeeDelegate() {
        if (feeDelegate == null) {
            switch (type) {
                case STD:
                    feeDelegate = new StdFee(a, b, c, d, amountType);
                    break;
                case CUSTOM:
                    feeDelegate = CustomFee.getInstance();
                    break;
                default:
                    throw new UnsupportedOperationException("type " + type + " is not supported");
            }
        }
        return feeDelegate;
    }
}
