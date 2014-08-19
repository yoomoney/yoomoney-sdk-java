package com.yandex.money.api.model;

import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;

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
 * @author Slava Yasevich (vyasevich@yamoney.ru)
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

    private final String name;

    /**
     * Constructor.
     *
     * @param name the name of a scope
     */
    protected Scope(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new NullPointerException("scope name can not be null or empty");
        }
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Scope) {
            Scope scope = (Scope) obj;
            return name.equals(scope.name);
        } else {
            return false;
        }
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

        private MoneySourceScope() {
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
        public boolean equals(Object obj) {
            if (obj instanceof MoneySourceScope) {
                MoneySourceScope scope = (MoneySourceScope) obj;
                return wallet == scope.wallet && cards == scope.cards;
            } else {
                return false;
            }
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
                throw new IllegalArgumentException("duration must have positive value: " +
                        duration);
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
                throw new IllegalArgumentException("sum must have positive value:" +
                        sum.toPlainString());
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

        private final String patternId;
        private final String account;

        private PaymentScope(String patternId, String account) {
            super("payment");
            this.patternId = patternId;
            this.account = account;
        }

        public static PaymentScope createPaymentToPattern(String patternId) {
            if (Strings.isNullOrEmpty(patternId)) {
                throw new IllegalArgumentException("pattern id is null or empty");
            }
            return new PaymentScope(patternId, null);
        }

        public static PaymentScope createPaymentToAccount(String account) {
            if (Strings.isNullOrEmpty(account)) {
                throw new IllegalArgumentException("account is null or empty");
            }
            return new PaymentScope(null, account);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PaymentScope) {
                PaymentScope scope = (PaymentScope) obj;
                if (patternId == null) {
                    return scope.patternId == null && account.equals(scope.account);
                } else {
                    return scope.account == null && patternId.equals(scope.patternId);
                }
            } else {
                return false;
            }
        }

        @Override
        public String getQualifiedName() {
            return super.getQualifiedName() + (Strings.isNullOrEmpty(patternId) ?
                    "to-account(\"" + account + "\")" : "to-pattern(\"" + patternId + "\")") +
                    getLimit();
        }
    }
}
