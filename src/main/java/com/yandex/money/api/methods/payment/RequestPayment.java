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

package com.yandex.money.api.methods.payment;

import com.google.gson.annotations.SerializedName;
import com.yandex.money.api.methods.payment.params.PaymentParams;
import com.yandex.money.api.model.*;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.FirstApiRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.util.Enums;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Context of a payment.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class RequestPayment extends BaseRequestPayment {

    /**
     * Available for payment money sources.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("money_source")
    public final MoneySource moneySource;

    /**
     * Account's balance.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("balance")
    public final BigDecimal balance;

    /**
     * Status of recipient's account.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("recipient_account_status")
    public final AccountStatus recipientAccountStatus;

    /**
     * Type of recipient's account.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("recipient_account_type")
    public final AccountType recipientAccountType;

    /**
     * Protection code, if chosen to use it.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("protection_code")
    public final String protectionCode;

    /**
     * URI to unblock payer's account if it was blocked.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("account_unblock_uri")
    public final String accountUnblockUri;

    /**
     * External action URI.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("ext_action_uri")
    public final String extActionUri;

    /**
     * {@code true} if Yandex.Money choose the last attached account for p2p payment.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("multiple_recipients_found")
    public final Boolean multipleRecipientsFound;

    /**
     * Payment fees
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("fees")
    public final Fees fees;

    @SuppressWarnings("WeakerAccess")
    protected RequestPayment(Builder builder) {
        super(builder);
        switch (status) {
            case SUCCESS:
                checkNotNull(builder.balance, "balance");
                break;
            case REFUSED:
                if (error == Error.ACCOUNT_BLOCKED) {
                    checkNotNull(builder.accountUnblockUri, "accountUnblockUri");
                }
                if (error == Error.EXT_ACTION_REQUIRED) {
                    checkNotNull(builder.extActionUri, "extActionUri");
                }
                break;
        }
        this.moneySource = builder.moneySource;
        this.balance = builder.balance;
        this.recipientAccountStatus = builder.recipientAccountStatus;
        this.recipientAccountType = builder.recipientAccountType;
        this.protectionCode = builder.protectionCode;
        this.accountUnblockUri = builder.accountUnblockUri;
        this.extActionUri = builder.extActionUri;
        this.multipleRecipientsFound = builder.multipleRecipientsFound;
        this.fees = builder.fees;
    }

    public static final class MoneySource {

        @SerializedName("wallet")
        public final Wallet wallet;
        @SerializedName("cards")
        public final Cards cards;

        public MoneySource(Wallet wallet, Cards cards) {
            this.wallet = wallet;
            this.cards = cards;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MoneySource that = (MoneySource) o;

            //noinspection SimplifiableIfStatement
            if (wallet != null ? !wallet.equals(that.wallet) : that.wallet != null) return false;
            return cards != null ? cards.equals(that.cards) : that.cards == null;
        }

        @Override
        public int hashCode() {
            int result = wallet != null ? wallet.hashCode() : 0;
            result = 31 * result + (cards != null ? cards.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "MoneySource{" +
                    "wallet=" + wallet +
                    ", cards=" + cards +
                    '}';
        }
    }

    public static final class Cards {

        @SerializedName("allowed")
        public final boolean allowed;
        @SuppressWarnings("WeakerAccess")
        @SerializedName("csc_required")
        public final boolean cscRequired;
        @SerializedName("items")
        public final List<Card> items;

        public Cards(boolean allowed, boolean cscRequired, List<Card> items) {
            this.allowed = allowed;
            this.cscRequired = cscRequired;
            this.items = items;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cards cards = (Cards) o;

            if (allowed != cards.allowed) return false;
            //noinspection SimplifiableIfStatement
            if (cscRequired != cards.cscRequired) return false;
            return items != null ? items.equals(cards.items) : cards.items == null;
        }

        @Override
        public int hashCode() {
            int result = (allowed ? 1 : 0);
            result = 31 * result + (cscRequired ? 1 : 0);
            result = 31 * result + (items != null ? items.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Cards{" +
                    "allowed=" + allowed +
                    ", cscRequired=" + cscRequired +
                    ", items=" + items +
                    '}';
        }
    }

    /**
     * Test results.
     */
    public enum TestResult implements Enums.WithCode<TestResult> {

        SUCCESS("success"),
        ILLEGAL_PARAMS("illegal_params"),
        ILLEGAL_PARAM_LABEL("illegal_param_label"),
        ILLEGAL_PARAM_TO("illegal_param_to"),
        ILLEGAL_PARAM_AMOUNT("illegal_param_amount"),
        ILLEGAL_PARAM_AMOUNT_DUE("illegal_param_amount_due"),
        ILLEGAL_PARAM_COMMENT("illegal_param_comment"),
        ILLEGAL_PARAM_MESSAGE("illegal_param_message"),
        ILLEGAL_PARAM_EXPIRE_PERIOD("illegal_param_expire_period"),
        NOT_ENOUGH_FUNDS("not_enough_funds"),
        PAYMENT_REFUSED("payment_refused"),
        PAYEE_NOT_FOUND("payee_not_found"),
        AUTHORIZATION_REJECT("authorization_reject"),
        LIMIT_EXCEEDED("limit_exceeded"),
        ACCOUNT_BLOCKED("account_blocked"),
        EXT_ACTION_REQUIRED("ext_action_required");

        public final String code;

        TestResult(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public TestResult[] getValues() {
            return values();
        }
    }

    /**
     * Requests for a payment context.
     * <p/>
     * Authorized session required.
     */
    public static final class Request extends FirstApiRequest<RequestPayment> {

        /**
         * Use static methods to create
         * {@link RequestPayment.Request}.
         */
        private Request(String patternId, Map<String, String> paymentParameters) {
            super(RequestPayment.class);

            addParameter("pattern_id", patternId);
            addParameters(paymentParameters);
        }

        /**
         * Creates instance of payment's request for general purposes. In other words for payments
         * to a specific pattern_id with known parameters. Consider to use implementations of
         * {@link PaymentParams} especially for p2p and phone-topup payments.
         *
         * @param patternId pattern_id (p2p, phone-topup or shop).
         * @param params    payment parameters.
         * @return new request instance.
         */
        public static Request newInstance(String patternId, Map<String, String> params) {
            return new Request(checkNotEmpty(patternId, "patternId"), checkNotEmpty(params, "params"));
        }

        /**
         * Creates instance of payment's request by providing convenience wrapper.
         *
         * @param paymentParams payment parameters wrapper.
         * @return new request instance.
         */
        public static Request newInstance(PaymentParams paymentParams) {
            return newInstance(checkNotNull(paymentParams, "paymentParams").patternId, paymentParams.paymentParams);
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/request-payment";
        }

        /**
         * Sets if test card is available. Automatically sets {@code test_payment} parameter.
         *
         * @return itself
         */
        public Request testCardAvailable() {
            addParameter("test_payment", true);
            addParameter("test_card", true);
            return this;
        }

        /**
         * Required test result. Automatically sets {@code test_payment} parameter.
         *
         * @param testResult test result
         */
        public Request setTestResult(TestResult testResult) {
            addParameter("test_payment", true);
            addParameter("test_result", testResult.code);
            return this;
        }
    }

    /**
     * Builds {@link RequestPayment} instance.
     */
    public final static class Builder extends BaseRequestPayment.Builder {

        MoneySource moneySource;
        BigDecimal balance;
        AccountStatus recipientAccountStatus;
        AccountType recipientAccountType;
        String protectionCode;
        String accountUnblockUri;
        String extActionUri;
        Boolean multipleRecipientsFound;
        Fees fees;

        public Builder setMoneySources(MoneySource moneySource) {
            this.moneySource = moneySource;
            return this;
        }

        public Builder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder setRecipientAccountStatus(AccountStatus recipientAccountStatus) {
            this.recipientAccountStatus = recipientAccountStatus;
            return this;
        }

        public Builder setRecipientAccountType(AccountType recipientAccountType) {
            this.recipientAccountType = recipientAccountType;
            return this;
        }

        public Builder setProtectionCode(String protectionCode) {
            this.protectionCode = protectionCode;
            return this;
        }

        public Builder setAccountUnblockUri(String accountUnblockUri) {
            this.accountUnblockUri = accountUnblockUri;
            return this;
        }

        public Builder setExtActionUri(String extActionUri) {
            this.extActionUri = extActionUri;
            return this;
        }

        public Builder setMultipleRecipientsFound(Boolean multipleRecipientsFound) {
            this.multipleRecipientsFound = multipleRecipientsFound;
            return this;
        }

        public Builder setFees(Fees fees) {
            this.fees = fees;
            return this;
        }

        @Override
        public RequestPayment create() {
            return new RequestPayment(this);
        }
    }
}
