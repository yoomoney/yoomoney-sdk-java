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

package com.yoo.money.api.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Detailed balance info.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public class BalanceDetails {

    public static final BalanceDetails ZERO = new BalanceDetails();

    /**
     * total balance
     */
    @SerializedName("total")
    public final BigDecimal total;

    /**
     * available balance
     */
    @SerializedName("available")
    public final BigDecimal available;

    /**
     * pending deposition
     */
    @SerializedName("deposition_pending")
    public final BigDecimal depositionPending;

    /**
     * money blocked
     */
    @SerializedName("blocked")
    public final BigDecimal blocked;

    /**
     * account's debt
     */
    @SerializedName("debt")
    public final BigDecimal debt;

    /**
     * money on hold
     */
    @SerializedName("hold")
    public final BigDecimal hold;

    BalanceDetails(Builder builder) {
        total = checkNotNull(builder.total, "total");
        available = checkNotNull(builder.available, "available");
        depositionPending = builder.depositionPending;
        blocked = builder.blocked;
        debt = builder.debt;
        hold = builder.hold;
    }

    private BalanceDetails() {
        total = BigDecimal.ZERO;
        available = BigDecimal.ZERO;
        depositionPending = null;
        blocked = null;
        debt = null;
        hold = null;
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

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal available = BigDecimal.ZERO;
        BigDecimal depositionPending;
        BigDecimal blocked;
        BigDecimal debt;
        BigDecimal hold;

        public Builder setTotal(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Builder setAvailable(BigDecimal available) {
            this.available = checkNotNull(available, "available");
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
