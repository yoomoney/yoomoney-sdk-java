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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import static com.yoo.money.api.util.Common.checkNotEmpty;

/**
 * Access token's scope. Provides simple and customizable scopes for every occasion. Complete list
 * of scopes is implemented as const fields or creator methods.
 * <p/>
 * List of scopes:
 * <li/>account-info - {@link #ACCOUNT_INFO};
 * <li/>incoming-transfers - {@link #INCOMING_TRANSFERS};
 * <li/>money-source - {@link #createMoneySourceScope()};
 * <li/>operation-details - {@link #OPERATION_DETAILS};
 * <li/>operation-history - {@link #OPERATION_HISTORY};
 * <li/>payment - {@link #createPaymentScopeToPattern(String)} or
 * {@link #createPaymentScopeToAccount(String)};
 * <li/>payment-p2p - {@link #PAYMENT_P2P} or {@link #createPaymentP2pLimitedScope()};
 * <li/>payment-shop - {@link #PAYMENT_SHOP} or {@link #createPaymentShopLimitedScope()}.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public class Scope {

    /**
     * Allow to get account info: account number, balance, avatar, etc.
     */
    public static final Scope ACCOUNT_INFO = new Scope("account-info");
    /**
     * Allow to accept and reject incoming transfers.
     */
    public static final Scope INCOMING_TRANSFERS = new Scope("incoming-transfers");
    /**
     * Allow to get operation details.
     */
    public static final Scope OPERATION_DETAILS = new Scope("operation-details");
    /**
     * Allow to get operation history.
     */
    public static final Scope OPERATION_HISTORY = new Scope("operation-history");
    /**
     * Allow to do transfer payments.
     */
    public static final Scope PAYMENT_P2P = new Scope("payment-p2p");
    /**
     * Allow to pay to shops.
     */
    public static final Scope PAYMENT_SHOP = new Scope("payment-shop");

    public final String name;

    /**
     * Constructor.
     *
     * @param name the name of a scope
     */
    protected Scope(String name) {
        this.name = checkNotEmpty(name, "name");
    }

    /**
     * Creates {@code money-source} scope: wallet is default.
     *
     * @return money source scope
     */
    public static MoneySourceScope createMoneySourceScope() {
        return new MoneySourceScope();
    }

    /**
     * Creates {@link #PAYMENT_P2P} scope that can be limited.
     *
     * @return scope
     */
    public static LimitedScope createPaymentP2pLimitedScope() {
        return new LimitedScope(PAYMENT_P2P.name);
    }

    /**
     * Creates {@link #PAYMENT_SHOP} scope that can be limited.
     *
     * @return scope
     */
    public static LimitedScope createPaymentShopLimitedScope() {
        return new LimitedScope(PAYMENT_SHOP.name);
    }

    /**
     * Creates {@code payment} scope to a specific account.
     *
     * @param account account number, phone number or email of a registered user
     * @return payment scope
     */
    public static PaymentScope createPaymentScopeToAccount(String account) {
        return PaymentScope.createPaymentToAccount(account);
    }

    /**
     * Creates {@code payment} scope to a specific shop (e.g. pattern)
     *
     * @param patternId pattern id
     * @return payment scope
     */
    public static PaymentScope createPaymentScopeToPattern(String patternId) {
        return PaymentScope.createPaymentToPattern(patternId);
    }

    /**
     * Creates {@code scope} parameter from iterator.
     *
     * @param scopes some scopes
     * @return parameter's value
     */
    public static String createScopeParameter(Collection<Scope> scopes) {
        checkNotEmpty(scopes, "scopes");
        Iterator<Scope> iterator = scopes.iterator();
        StringBuilder builder = new StringBuilder(iterator.next().getQualifiedName());
        while (iterator.hasNext()) {
            builder.append(" ").append(iterator.next().getQualifiedName());
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scope scope = (Scope) o;

        return name.equals(scope.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Scope{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * @return fully qualified name for use in requests
     */
    public String getQualifiedName() {
        return name;
    }

    public static final class MoneySourceScope extends Scope {

        private boolean wallet = true;
        private boolean cards = false;

        MoneySourceScope() {
            super("money-source");
        }

        /**
         * Sets available sources.
         * <p/>
         * If {@code wallet == false && cards == false} exception will be thrown.
         *
         * @param wallet {@code true} if wallet is allowed
         * @param cards {@code true} if cards are allowed
         * @return scope
         */
        public MoneySourceScope setSources(boolean wallet, boolean cards) {
            if (!(wallet || cards)) {
                throw new IllegalArgumentException("inconsistent arguments: at least one of" +
                        "parameters should not be false");
            }
            this.wallet = wallet;
            this.cards = cards;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MoneySourceScope that = (MoneySourceScope) o;

            return wallet == that.wallet && cards == that.cards;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (wallet ? 1 : 0);
            result = 31 * result + (cards ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "MoneySourceScope{" +
                    "name='" + name + '\'' +
                    ", wallet=" + wallet +
                    ", cards=" + cards +
                    '}';
        }

        @Override
        public String getQualifiedName() {
            return super.getQualifiedName() + "(\"" + (wallet ? "wallet" : "") +
                    (wallet && cards ? "\",\"" : "") + (cards ? "cards" : "") + "\")";
        }
    }

    public static class LimitedScope extends Scope {

        private Integer duration;
        private BigDecimal sum;

        protected LimitedScope(String name) {
            super(name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            LimitedScope that = (LimitedScope) o;

            return !(duration != null ? !duration.equals(that.duration) : that.duration != null) &&
                    !(sum != null ? !sum.equals(that.sum) : that.sum != null);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (duration != null ? duration.hashCode() : 0);
            result = 31 * result + (sum != null ? sum.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LimitedScope{" +
                    "name='" + name + '\'' +
                    ", duration=" + duration +
                    ", sum=" + sum +
                    '}';
        }

        @Override
        public String getQualifiedName() {
            return super.getQualifiedName() + getLimit();
        }

        /**
         * Sets duration.
         *
         * @param duration positive number
         * @return scope
         */
        public LimitedScope setDuration(Integer duration) {
            if (duration != null && duration < 1) {
                throw new IllegalArgumentException("duration must be a positive number: " + duration);
            }
            this.duration = duration;
            return this;
        }

        /**
         * Sets max sum.
         *
         * @param sum positive value
         * @return scope
         */
        public LimitedScope setSum(BigDecimal sum) {
            if (sum != null && sum.doubleValue() < 0) {
                throw new IllegalArgumentException("sum must be a positive number:" + sum.toPlainString());
            }
            this.sum = sum;
            return this;
        }

        /**
         * @return limit modifier
         */
        protected String getLimit() {
            return sum != null ? ".limit(" + (duration != null ? duration : "") + "," +
                    sum.toPlainString() + ")" : "";
        }
    }

    public static final class PaymentScope extends LimitedScope {

        private final String parameter;

        private PaymentScope(String name, String parameter) {
            super(name);
            this.parameter = checkNotEmpty(parameter, "parameter");
        }

        public static PaymentScope createPaymentToPattern(String patternId) {
            return new PaymentScope("payment.to-pattern(\"%1$s\")", patternId);
        }

        public static PaymentScope createPaymentToAccount(String account) {
            return new PaymentScope("payment.to-account(\"%1$s\")", account);
        }

        @Override
        public String getQualifiedName() {
            return String.format(super.getQualifiedName(), parameter);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            PaymentScope that = (PaymentScope) o;

            return parameter.equals(that.parameter);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + parameter.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "PaymentScope{" +
                    "parameter='" + parameter + '\'' +
                    '}';
        }
    }
}
