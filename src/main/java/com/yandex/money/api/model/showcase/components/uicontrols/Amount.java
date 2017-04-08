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

package com.yandex.money.api.model.showcase.components.uicontrols;

import com.yandex.money.api.model.Currency;
import com.yandex.money.api.model.showcase.Fee;

import java.math.BigDecimal;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Cost of transaction.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Amount extends Number {

    /**
     * Currency. Default is {@link Currency#RUB}.
     */
    public final Currency currency;

    /**
     * Fee.
     */
    public final Fee fee;

    protected Amount(Builder builder) {
        super(builder);
        currency = checkNotNull(builder.currency, "currency");
        fee = builder.fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Amount amount = (Amount) o;

        if (currency != amount.currency) return false;
        return fee != null ? fee.equals(amount.fee) : amount.fee == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + (fee != null ? fee.hashCode() : 0);
        return result;
    }

    /**
     * {@link Amount} builder.
     */
    public static class Builder extends Number.Builder {

        private static final BigDecimal PENNY = new BigDecimal("0.01");

        Currency currency = Currency.RUB;
        Fee fee;

        public Builder() {
            super(PENNY, null, PENNY);
        }

        @Override
        public Amount create() {
            return new Amount(this);
        }

        @Override
        public Builder setMin(BigDecimal min) {
            if (min != null) {
                super.setMin(min);
            }
            return this;
        }

        public Builder setCurrency(Currency currency) {
            if (currency != null) {
                this.currency = currency;
            }
            return this;
        }

        public Builder setFee(Fee fee) {
            if (fee != null) {
                this.fee = fee;
            }
            return this;
        }
    }
}