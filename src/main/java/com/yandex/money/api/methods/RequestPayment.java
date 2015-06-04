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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.params.Params;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.model.Wallet;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.utils.Strings;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Context of a payment.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class RequestPayment extends BaseRequestPayment {

    public final List<MoneySource> moneySources;
    public final boolean cscRequired;
    public final BigDecimal balance;
    public final AccountStatus recipientAccountStatus;
    public final AccountType recipientAccountType;
    public final String protectionCode;
    public final String accountUnblockUri;
    public final String extActionUri;

    /**
     * Use builder to create an instance.
     */
    private RequestPayment(Status status, Error error, List<MoneySource> moneySources,
                           Boolean cscRequired, String requestId, BigDecimal contractAmount,
                           BigDecimal balance, AccountStatus recipientAccountStatus,
                           AccountType recipientAccountType, String protectionCode,
                           String accountUnblockUri, String extActionUri) {

        super(status, error, requestId, contractAmount);
        if (moneySources == null) {
            throw new NullPointerException("moneySources is null");
        }
        this.moneySources = Collections.unmodifiableList(moneySources);
        this.cscRequired = cscRequired != null && cscRequired;
        this.balance = balance;
        this.recipientAccountStatus = recipientAccountStatus;
        this.recipientAccountType = recipientAccountType;
        this.protectionCode = protectionCode;
        this.accountUnblockUri = accountUnblockUri;
        this.extActionUri = extActionUri;
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
         *
         * {@link com.yandex.money.api.methods.RequestPayment.Request}.
         */
        private Request(String patternId, Map<String, String> paymentParameters) {
            super(RequestPayment.class, new Deserializer());

            addParameter("pattern_id", patternId);
            addParameters(paymentParameters);
        }

        /**
         * Creates instance of payment's request for general purposes. In other words for payments
         * to a specific pattern_id with known parameters. Consider to use implementations of
         * {@link Params} especially for p2p and phone-topup payments.
         *
         * @param patternId pattern_id (p2p, phone-topup or shop).
         * @param params shop parameters.
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
         * @param paymentParams payment params
         * @return new request instance
         */
        public static Request newInstance(Params paymentParams) {
            if (paymentParams == null) {
                throw new IllegalArgumentException("paymentParams is null");
            }
            return Request.newInstance(paymentParams.getPatternId(), paymentParams.makeParams());
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
     * Builds {@link com.yandex.money.api.methods.RequestPayment} instance.
     */
    public static class Builder {
        private Status status;
        private Error error;
        private List<MoneySource> moneySources;
        private Boolean cscRequired;
        private String requestId;
        private BigDecimal contractAmount;
        private BigDecimal balance;
        private AccountStatus recipientAccountStatus;
        private AccountType recipientAccountType;
        private String protectionCode;
        private String accountUnblockUri;
        private String extActionUri;

        /**
         * @param status status of request
         */
        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        /**
         * @param error error code
         */
        public Builder setError(Error error) {
            this.error = error;
            return this;
        }

        /**
         * @param moneySources list of available for payment money sources
         */
        public Builder setMoneySources(List<MoneySource> moneySources) {
            this.moneySources = moneySources;
            return this;
        }

        /**
         * @param cscRequired {@code true} if Card Security Code is required
         */
        public Builder setCscRequired(Boolean cscRequired) {
            this.cscRequired = cscRequired;
            return this;
        }

        /**
         * @param requestId request id
         */
        public Builder setRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        /**
         * @param contractAmount total amount of money that payer will be charged
         */
        public Builder setContractAmount(BigDecimal contractAmount) {
            this.contractAmount = contractAmount;
            return this;
        }

        /**
         * @param balance account's balance
         */
        public Builder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        /**
         * @param recipientAccountStatus status of recipient's account
         */
        public Builder setRecipientAccountStatus(AccountStatus recipientAccountStatus) {
            this.recipientAccountStatus = recipientAccountStatus;
            return this;
        }

        /**
         * @param recipientAccountType type of recipient's account
         */
        public Builder setRecipientAccountType(AccountType recipientAccountType) {
            this.recipientAccountType = recipientAccountType;
            return this;
        }

        /**
         * @param protectionCode protection code, if chosen to use it
         */
        public Builder setProtectionCode(String protectionCode) {
            this.protectionCode = protectionCode;
            return this;
        }

        /**
         * @param accountUnblockUri URI to unblock payer's account if it was blocked
         */
        public Builder setAccountUnblockUri(String accountUnblockUri) {
            this.accountUnblockUri = accountUnblockUri;
            return this;
        }

        /**
         * @param extActionUri external action URI
         */
        public Builder setExtActionUri(String extActionUri) {
            this.extActionUri = extActionUri;
            return this;
        }

        /**
         * @return {@link com.yandex.money.api.methods.RequestPayment}
         */
        public RequestPayment createRequestPayment() {
            return new RequestPayment(status, error,
                    moneySources == null ? new ArrayList<MoneySource>() : moneySources, cscRequired,
                    requestId, contractAmount, balance, recipientAccountStatus,
                    recipientAccountType, protectionCode, accountUnblockUri, extActionUri);
        }
    }

    private static final class Deserializer implements JsonDeserializer<RequestPayment> {
        @Override
        public RequestPayment deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            JsonObject moneySource = object.getAsJsonObject("money_source");
            List<MoneySource> moneySources = new ArrayList<>();
            Boolean cscRequired = null;
            if (moneySource != null) {
                final String walletMember = "wallet";
                if (moneySource.has(walletMember)) {
                    JsonObject wallet = moneySource.getAsJsonObject(walletMember);
                    if (JsonUtils.getMandatoryBoolean(wallet, "allowed")) {
                        moneySources.add(new Wallet());
                    }
                }
                final String cardsMember = "cards";
                if (moneySource.has(cardsMember)) {
                    JsonObject cards = moneySource.getAsJsonObject(cardsMember);
                    if (JsonUtils.getMandatoryBoolean(cards, "allowed")) {
                        cscRequired = JsonUtils.getMandatoryBoolean(cards, "csc_required");
                        JsonArray items = cards.getAsJsonArray("items");
                        for (JsonElement item : items) {
                            moneySources.add(Card.createFromJson(item));
                        }
                    }
                }
            }
            return new Builder()
                    .setStatus(Status.parse(JsonUtils.getMandatoryString(object, MEMBER_STATUS)))
                    .setError(Error.parse(JsonUtils.getString(object, MEMBER_ERROR)))
                    .setMoneySources(moneySources)
                    .setCscRequired(cscRequired)
                    .setRequestId(JsonUtils.getString(object, MEMBER_REQUEST_ID))
                    .setContractAmount(JsonUtils.getBigDecimal(object, MEMBER_CONTRACT_AMOUNT))
                    .setBalance(JsonUtils.getBigDecimal(object, "balance"))
                    .setRecipientAccountStatus(AccountStatus.parse(
                            JsonUtils.getString(object, "recipient_account_status")))
                    .setRecipientAccountType(AccountType.parse(
                            JsonUtils.getString(object, "recipient_account_type")))
                    .setProtectionCode(JsonUtils.getString(object, "protection_code"))
                    .setAccountUnblockUri(JsonUtils.getString(object, "account_unblock_uri"))
                    .setExtActionUri(JsonUtils.getString(object, "ext_action_uri"))
                    .createRequestPayment();
        }
    }
}
