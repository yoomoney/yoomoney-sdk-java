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

package com.yandex.money.api.methods;

import com.yandex.money.api.methods.params.PaymentParams;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.typeadapters.RequestPaymentTypeAdapter;
import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Context of a payment.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class RequestPayment extends BaseRequestPayment {

    /**
     * List of available for payment money sources.
     */
    public final List<MoneySource> moneySources;

    /**
     * Card Security Code. Required if i{@code true}.
     */
    public final boolean cscRequired;

    /**
     * Account's balance.
     */
    public final BigDecimal balance;

    /**
     * Status of recipient's account.
     */
    public final AccountStatus recipientAccountStatus;

    /**
     * Type of recipient's account.
     */
    public final AccountType recipientAccountType;

    /**
     * Protection code, if chosen to use it.
     */
    public final String protectionCode;

    /**
     * URI to unblock payer's account if it was blocked.
     */
    public final String accountUnblockUri;

    /**
     * External action URI.
     */
    public final String extActionUri;

    private RequestPayment(Builder builder) {
        super(builder);
        if (builder.moneySources == null) {
            throw new NullPointerException("moneySources is null");
        }
        this.moneySources = Collections.unmodifiableList(builder.moneySources);
        this.cscRequired = builder.cscRequired;
        this.balance = builder.balance;
        this.recipientAccountStatus = builder.recipientAccountStatus;
        this.recipientAccountType = builder.recipientAccountType;
        this.protectionCode = builder.protectionCode;
        this.accountUnblockUri = builder.accountUnblockUri;
        this.extActionUri = builder.extActionUri;
    }

    @Override
    public String toString() {
        return super.toString() + "RequestPayment{" +
                "moneySources=" + moneySources +
                ", cscRequired=" + cscRequired +
                ", balance=" + balance +
                ", recipientAccountStatus=" + recipientAccountStatus +
                ", recipientAccountType=" + recipientAccountType +
                ", protectionCode='" + protectionCode + '\'' +
                ", accountUnblockUri='" + accountUnblockUri + '\'' +
                ", extActionUri='" + extActionUri + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestPayment that = (RequestPayment) o;

        return cscRequired == that.cscRequired && moneySources.equals(that.moneySources) &&
                !(balance != null ? !balance.equals(that.balance) : that.balance != null) &&
                recipientAccountStatus == that.recipientAccountStatus &&
                recipientAccountType == that.recipientAccountType &&
                !(protectionCode != null ? !protectionCode.equals(that.protectionCode) :
                        that.protectionCode != null) &&
                !(accountUnblockUri != null ? !accountUnblockUri.equals(that.accountUnblockUri) :
                        that.accountUnblockUri != null) &&
                !(extActionUri != null ? !extActionUri.equals(that.extActionUri) :
                        that.extActionUri != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + moneySources.hashCode();
        result = 31 * result + (cscRequired ? 1 : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (recipientAccountStatus != null ?
                recipientAccountStatus.hashCode() : 0);
        result = 31 * result + (recipientAccountType != null ? recipientAccountType.hashCode() : 0);
        result = 31 * result + (protectionCode != null ? protectionCode.hashCode() : 0);
        result = 31 * result + (accountUnblockUri != null ? accountUnblockUri.hashCode() : 0);
        result = 31 * result + (extActionUri != null ? extActionUri.hashCode() : 0);
        return result;
    }

    /**
     * Test results.
     */
    public enum TestResult {

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
    }

    /**
     * Requests for a payment context.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<RequestPayment> {

        /**
         * Use static methods to create
         * {@link com.yandex.money.api.methods.RequestPayment.Request}.
         */
        private Request(String patternId, Map<String, String> paymentParameters) {
            super(RequestPayment.class, RequestPaymentTypeAdapter.getInstance());

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
            Strings.checkNotNullAndNotEmpty(patternId, "patternId");
            if (params == null || params.isEmpty()) {
                throw new IllegalArgumentException("params is null or empty");
            }
            return new Request(patternId, params);
        }

        /**
         * Creates instance of payment's request by providing convenience wrapper.
         *
         * @param paymentParams payment parameters wrapper.
         * @return new request instance.
         */
        public static Request newInstance(PaymentParams paymentParams) {
            if (paymentParams == null) {
                throw new IllegalArgumentException("paymentParams is null");
            }
            return newInstance(paymentParams.getPatternId(), paymentParams.makeParams());
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
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
     * Builds {@link com.yandex.money.api.methods.RequestPayment} instance.
     */
    public final static class Builder extends BaseRequestPayment.Builder {

        private List<MoneySource> moneySources;
        private boolean cscRequired;
        private BigDecimal balance;
        private AccountStatus recipientAccountStatus;
        private AccountType recipientAccountType;
        private String protectionCode;
        private String accountUnblockUri;
        private String extActionUri;

        public Builder setMoneySources(List<MoneySource> moneySources) {
            this.moneySources = moneySources;
            return this;
        }

        public Builder setCscRequired(Boolean cscRequired) {
            this.cscRequired = cscRequired;
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

        @Override
        public RequestPayment create() {
            return new RequestPayment(this);
        }
    }
}
