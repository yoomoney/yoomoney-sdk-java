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

package com.yandex.money.api.model;

import java.math.BigDecimal;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Detailed balance info.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class BalanceDetails {

    /**
     * total balance
     */
    public final BigDecimal total;

    /**
     * available balance
     */
    public final BigDecimal available;

    /**
     * pending deposition
     */
    public final BigDecimal depositionPending;

    /**
     * money blocked
     */
    public final BigDecimal blocked;

    /**
     * account's debt
     */
    public final BigDecimal debt;

    /**
     * money on hold
     */
    public final BigDecimal hold;

    private BalanceDetails(Builder builder) {
        checkNotNull(builder.total, "total");
        checkNotNull(builder.available, "available");

        this.total = builder.total;
        this.available = builder.available;
        this.depositionPending = builder.depositionPending;
        this.blocked = builder.blocked;
        this.debt = builder.debt;
        this.hold = builder.hold;
    }

    @Override
    public String toString() {
        return "BalanceDetails{" +
                "total=" + total +
                ", available=" + available +
                ", depositionPending=" + depositionPending +
                ", blocked=" + blocked +
                ", debt=" + debt +
                ", hold=" + hold +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceDetails that = (BalanceDetails) o;

        return total.equals(that.total) &&
                available.equals(that.available) &&
                !(depositionPending != null ? !depositionPending.equals(that.depositionPending)
                        : that.depositionPending != null) &&
                !(blocked != null ? !blocked.equals(that.blocked) : that.blocked != null) &&
                !(debt != null ? !debt.equals(that.debt) : that.debt != null) &&
                !(hold != null ? !hold.equals(that.hold) : that.hold != null);
    }

    @Override
    public int hashCode() {
        int result = total.hashCode();
        result = 31 * result + available.hashCode();
        result = 31 * result + (depositionPending != null ? depositionPending.hashCode() : 0);
        result = 31 * result + (blocked != null ? blocked.hashCode() : 0);
        result = 31 * result + (debt != null ? debt.hashCode() : 0);
        result = 31 * result + (hold != null ? hold.hashCode() : 0);
        return result;
    }

    public static final class Builder {

        private BigDecimal total;
        private BigDecimal available;
        private BigDecimal depositionPending;
        private BigDecimal blocked;
        private BigDecimal debt;
        private BigDecimal hold;

        public Builder setTotal(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Builder setAvailable(BigDecimal available) {
            this.available = available;
            return this;
        }

        public Builder setDepositionPending(BigDecimal depositionPending) {
            this.depositionPending = depositionPending;
            return this;
        }

        public Builder setBlocked(BigDecimal blocked) {
            this.blocked = blocked;
            return this;
        }

        public Builder setDebt(BigDecimal debt) {
            this.debt = debt;
            return this;
        }

        public Builder setHold(BigDecimal hold) {
            this.hold = hold;
            return this;
        }

        public BalanceDetails create() {
            return new BalanceDetails(this);
        }
    }
}
