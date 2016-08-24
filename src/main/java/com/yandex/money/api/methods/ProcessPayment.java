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

import com.yandex.money.api.model.DigitalGoods;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.methods.ProcessPaymentTypeAdapter;
import com.yandex.money.api.utils.Enums;

import java.math.BigDecimal;

import static com.yandex.money.api.utils.Common.checkNotEmpty;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Process payment.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ProcessPayment extends BaseProcessPayment {

    /**
     * Payment id.
     */
    public final String paymentId;

    /**
     * Account's balance.
     */
    public final BigDecimal balance;

    /**
     * Payer's account number.
     */
    public final String payer;

    /**
     * Payee's account number.
     */
    public final String payee;

    /**
     * Amount payee will receive.
     */
    public final BigDecimal creditAmount;

    /**
     * URL for locked accounts URI that can be used to unlock it.
     */
    public final String accountUnblockUri;

    public final String payeeUid;

    /**
     * Link for payment receiving.
     */
    public final String holdForPickupLink;

    /**
     * Received digital goods.
     */
    public final DigitalGoods digitalGoods;

    /**
     * Use {@link com.yandex.money.api.methods.ProcessPayment.Builder} to create an instance.
     */
    private ProcessPayment(Builder builder) {
        super(builder);
        switch (status) {
            case SUCCESS:
                checkNotNull(builder.paymentId, "paymentId");
                checkNotNull(builder.balance, "balance");
                break;
            case REFUSED:
                if(error == Error.ACCOUNT_BLOCKED) {
                    checkNotNull(builder.accountUnblockUri, "accountUnblockUri");
                }
                break;
        }

        this.paymentId = builder.paymentId;
        this.balance = builder.balance;
        this.payer = builder.payer;
        this.payee = builder.payee;
        this.creditAmount = builder.creditAmount;
        this.accountUnblockUri = builder.accountUnblockUri;
        this.payeeUid = builder.payeeUid;
        this.holdForPickupLink = builder.holdForPickupLink;
        this.digitalGoods = builder.digitalGoods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProcessPayment that = (ProcessPayment) o;

        return !(paymentId != null ? !paymentId.equals(that.paymentId) : that.paymentId != null) &&
                !(balance != null ? !balance.equals(that.balance) : that.balance != null) &&
                !(payer != null ? !payer.equals(that.payer) : that.payer != null) &&
                !(payee != null ? !payee.equals(that.payee) : that.payee != null) &&
                !(creditAmount != null ? !creditAmount.equals(that.creditAmount) :
                        that.creditAmount != null) &&
                !(accountUnblockUri != null ? !accountUnblockUri.equals(that.accountUnblockUri) :
                        that.accountUnblockUri != null) &&
                !(payeeUid != null ? !payeeUid.equals(that.payeeUid) : that.payeeUid != null) &&
                !(holdForPickupLink != null ? !holdForPickupLink.equals(that.holdForPickupLink) :
                        that.holdForPickupLink != null) &&
                !(digitalGoods != null ? !digitalGoods.equals(that.digitalGoods) :
                        that.digitalGoods != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (paymentId != null ? paymentId.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (payer != null ? payer.hashCode() : 0);
        result = 31 * result + (payee != null ? payee.hashCode() : 0);
        result = 31 * result + (creditAmount != null ? creditAmount.hashCode() : 0);
        result = 31 * result + (accountUnblockUri != null ? accountUnblockUri.hashCode() : 0);
        result = 31 * result + (payeeUid != null ? payeeUid.hashCode() : 0);
        result = 31 * result + (holdForPickupLink != null ? holdForPickupLink.hashCode() : 0);
        result = 31 * result + (digitalGoods != null ? digitalGoods.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "ProcessPayment{" +
                "paymentId='" + paymentId + '\'' +
                ", balance=" + balance +
                ", payer='" + payer + '\'' +
                ", payee='" + payee + '\'' +
                ", creditAmount=" + creditAmount +
                ", accountUnblockUri='" + accountUnblockUri + '\'' +
                ", payeeUid='" + payeeUid + '\'' +
                ", holdForPickupLink='" + holdForPickupLink + '\'' +
                ", digitalGoods=" + digitalGoods +
                '}';
    }

    /**
     * Available test results.
     */
    public enum TestResult implements Enums.WithCode<TestResult> {

        SUCCESS("success"),
        CONTRACT_NOT_FOUND("contract_not_found"),
        NOT_ENOUGH_FUNDS("not_enough_funds"),
        LIMIT_EXCEEDED("limit_exceeded"),
        MONEY_SOURCE_NOT_AVAILABLE("money_source_not_available"),
        ILLEGAL_PARAM_CSC("illegal_param_csc"),
        PAYMENT_REFUSED("payment_refused"),
        AUTHORIZATION_REJECT("authorization_reject"),
        ACCOUNT_BLOCKED("account_blocked"),
        ILLEGAL_PARAM_EXT_AUTH_SUCCESS_URI("illegal_param_ext_auth_success_uri"),
        ILLEGAL_PARAM_EXT_AUTH_FAIL_URI("illegal_param_ext_auth_fail_uri");

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
     * Request for payment processing.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<ProcessPayment> {

        /**
         * Repeat request using the same request id. This is used when {@link ProcessPayment} is in
         * {@code IN_PROGRESS} state.
         *
         * @param requestId request id
         */
        public Request(String requestId) {
            this(requestId, null, null, null, null);
        }

        /**
         * First request for payment processing.
         *
         * @param requestId request id
         * @param moneySource selected money source
         * @param csc Card Security Code if selected money source requires it
         * @param extAuthSuccessUri uri which will be used for redirection if operation is
         *                          successful
         * @param extAuthFailUri uri which will be used for redirection if operation is failed
         */
        public Request(String requestId, MoneySource moneySource, String csc, String extAuthSuccessUri,
                       String extAuthFailUri) {

            super(ProcessPaymentTypeAdapter.getInstance());
            addParameter("request_id", checkNotEmpty(requestId, "requestId"));
            addParameter("csc", csc);
            addParameter("ext_auth_success_uri", extAuthSuccessUri);
            addParameter("ext_auth_fail_uri", extAuthFailUri);
            if (moneySource != null) {
                addParameter("money_source", moneySource.id);
            }
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/process-payment";
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
     * Builder for {@link com.yandex.money.api.methods.ProcessPayment}.
     */
    public static final class Builder extends BaseProcessPayment.Builder {

        private String paymentId;
        private BigDecimal balance;
        private String payer;
        private String payee;
        private BigDecimal creditAmount;
        private String accountUnblockUri;
        private String payeeUid;
        private String holdForPickupLink;
        private DigitalGoods digitalGoods;

        public Builder setPaymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder setPayer(String payer) {
            this.payer = payer;
            return this;
        }

        public Builder setPayee(String payee) {
            this.payee = payee;
            return this;
        }

        public Builder setCreditAmount(BigDecimal creditAmount) {
            this.creditAmount = creditAmount;
            return this;
        }

        public Builder setAccountUnblockUri(String accountUnblockUri) {
            this.accountUnblockUri = accountUnblockUri;
            return this;
        }

        public Builder setPayeeUid(String payeeUid) {
            this.payeeUid = payeeUid;
            return this;
        }

        public Builder setHoldForPickupLink(String holdForPickupLink) {
            this.holdForPickupLink = holdForPickupLink;
            return this;
        }

        public Builder setDigitalGoods(DigitalGoods digitalGoods) {
            this.digitalGoods = digitalGoods;
            return this;
        }

        /**
         * Creates {@link ProcessPayment} instance.
         *
         * @return {@link ProcessPayment} instance
         */
        public ProcessPayment create() {
            return new ProcessPayment(this);
        }
    }
}
