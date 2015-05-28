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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.DigitalGoods;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Process payment.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ProcessPayment extends BaseProcessPayment {

    public final String paymentId;
    public final BigDecimal balance;
    public final String payer;
    public final String payee;
    public final BigDecimal creditAmount;
    public final String accountUnblockUri;
    public final String payeeUid;
    public final String holdForPickupLink;
    public final DigitalGoods digitalGoods;

    /**
     * Use {@link com.yandex.money.api.methods.ProcessPayment.Builder} to create an instance.
     */
    private ProcessPayment(Status status, Error error, String paymentId, BigDecimal balance,
                           String invoiceId, String payer, String payee, BigDecimal creditAmount,
                           String accountUnblockUri, String payeeUid, String holdForPickupLink,
                           String acsUri, Map<String, String> acsParams, Long nextRetry,
                           DigitalGoods digitalGoods) {

        super(status, error, invoiceId, acsUri, acsParams, nextRetry);
        if (status == Status.SUCCESS && paymentId == null) {
            throw new NullPointerException("paymentId is null when status is success");
        }
        this.paymentId = paymentId;
        this.balance = balance;
        this.payer = payer;
        this.payee = payee;
        this.creditAmount = creditAmount;
        this.accountUnblockUri = accountUnblockUri;
        this.payeeUid = payeeUid;
        this.holdForPickupLink = holdForPickupLink;
        this.digitalGoods = digitalGoods;
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
     * Request for payment processing.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request implements ApiRequest<ProcessPayment> {

        private final String requestId;
        private final MoneySource moneySource;
        private final String csc;
        private final String extAuthSuccessUri;
        private final String extAuthFailUri;

        private boolean testPayment;
        private boolean testCardAvailable;
        private TestResult testResult;

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
        public Request(String requestId, MoneySource moneySource, String csc,
                       String extAuthSuccessUri, String extAuthFailUri) {

            if (requestId == null || requestId.isEmpty()) {
                throw new IllegalArgumentException("requestId is null or empty");
            }
            this.requestId = requestId;
            this.moneySource = moneySource;
            this.csc = csc;
            this.extAuthSuccessUri = extAuthSuccessUri;
            this.extAuthFailUri = extAuthFailUri;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/process-payment");
        }

        @Override
        public ProcessPayment parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), ProcessPayment.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            PostRequestBodyBuffer postRequestBodyBuffer = new PostRequestBodyBuffer();
            if (moneySource != null) {
                postRequestBodyBuffer.addParam("money_source", moneySource.id);
            }
            if (testPayment) {
                postRequestBodyBuffer.addParamIfNotNull("test_payment", true);
                if (testCardAvailable) {
                    postRequestBodyBuffer.addParamIfNotNull("test_card", true);
                }
                if (testResult != null) {
                    postRequestBodyBuffer.addParam("test_result", testResult.code);
                }
            }
            return postRequestBodyBuffer
                    .addParam("request_id", requestId)
                    .addParamIfNotNull("csc", csc)
                    .addParamIfNotNull("ext_auth_success_uri", extAuthSuccessUri)
                    .addParamIfNotNull("ext_auth_fail_uri", extAuthFailUri);
        }

        /**
         * Sets if this is a test payment.
         *
         * @param testPayment {@code true} if test payment
         */
        public void setTestPayment(boolean testPayment) {
            this.testPayment = testPayment;
        }

        /**
         * Sets if test card is available
         *
         * @param testCardAvailable {@code true} if test card is available
         */
        public void setTestCardAvailable(boolean testCardAvailable) {
            this.testCardAvailable = testCardAvailable;
        }

        /**
         * Required test result.
         *
         * @param testResult test result
         */
        public void setTestResult(TestResult testResult) {
            this.testResult = testResult;
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(ProcessPayment.class, new Deserializer())
                    .create();
        }
    }

    /**
     * Available test results.
     */
    public enum TestResult {

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
    }

    /**
     * Builder for {@link com.yandex.money.api.methods.ProcessPayment}.
     */
    public static class Builder {
        private Status status;
        private Error error;
        private String paymentId;
        private BigDecimal balance;
        private String invoiceId;
        private String payer;
        private String payee;
        private BigDecimal creditAmount;
        private String accountUnblockUri;
        private String payeeUid;
        private String holdForPickupLink;
        private String acsUri;
        private Map<String, String> acsParams;
        private Long nextRetry;
        private DigitalGoods digitalGoods;

        /**
         * @param status status of process payment
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
         * @param paymentId payment id
         */
        public Builder setPaymentId(String paymentId) {
            this.paymentId = paymentId;
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
         * @param invoiceId invoice id of successful operation
         */
        public Builder setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        /**
         * @param payer payer's account number
         */
        public Builder setPayer(String payer) {
            this.payer = payer;
            return this;
        }

        /**
         * @param payee payee's account number
         */
        public Builder setPayee(String payee) {
            this.payee = payee;
            return this;
        }

        /**
         * @param creditAmount this amount payee will receive
         */
        public Builder setCreditAmount(BigDecimal creditAmount) {
            this.creditAmount = creditAmount;
            return this;
        }

        /**
         * @param accountUnblockUri for locked accounts URI that can be used to unlock it
         */
        public Builder setAccountUnblockUri(String accountUnblockUri) {
            this.accountUnblockUri = accountUnblockUri;
            return this;
        }

        public Builder setPayeeUid(String payeeUid) {
            this.payeeUid = payeeUid;
            return this;
        }

        /**
         * @param holdForPickupLink link for payment receiving
         */
        public Builder setHoldForPickupLink(String holdForPickupLink) {
            this.holdForPickupLink = holdForPickupLink;
            return this;
        }

        /**
         * @param acsUri address for 3D Secure authorization
         */
        public Builder setAcsUri(String acsUri) {
            this.acsUri = acsUri;
            return this;
        }

        /**
         * @param acsParams POST parameters for 3D Secure authorization (key-value pairs)
         */
        public Builder setAcsParams(Map<String, String> acsParams) {
            this.acsParams = acsParams;
            return this;
        }

        /**
         * @param nextRetry recommended time interval between process payment requests
         */
        public Builder setNextRetry(Long nextRetry) {
            this.nextRetry = nextRetry;
            return this;
        }

        /**
         * @param digitalGoods received digital goods
         */
        public Builder setDigitalGoods(DigitalGoods digitalGoods) {
            this.digitalGoods = digitalGoods;
            return this;
        }

        /**
         * Creates {@link ProcessPayment} instance.
         *
         * @return {@link ProcessPayment} instance
         */
        public ProcessPayment createProcessPayment() {
            return new ProcessPayment(status, error, paymentId, balance, invoiceId, payer, payee,
                    creditAmount, accountUnblockUri, payeeUid, holdForPickupLink, acsUri,
                    acsParams == null ? new HashMap<String, String>() : acsParams, nextRetry,
                    digitalGoods);
        }
    }

    private static final class Deserializer implements JsonDeserializer<ProcessPayment> {
        @Override
        public ProcessPayment deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new Builder()
                    .setStatus(Status.parse(JsonUtils.getMandatoryString(object, MEMBER_STATUS)))
                    .setError(Error.parse(JsonUtils.getString(object, MEMBER_ERROR)))
                    .setPaymentId(JsonUtils.getString(object, "payment_id"))
                    .setBalance(JsonUtils.getBigDecimal(object, "balance"))
                    .setInvoiceId(JsonUtils.getString(object, "invoice_id"))
                    .setPayer(JsonUtils.getString(object, "payer"))
                    .setPayee(JsonUtils.getString(object, "payee"))
                    .setCreditAmount(JsonUtils.getBigDecimal(object, "credit_amount"))
                    .setAccountUnblockUri(JsonUtils.getString(object, "account_unblock_uri"))
                    .setPayeeUid(JsonUtils.getString(object, "payee_uid"))
                    .setHoldForPickupLink(JsonUtils.getString(object, "hold_for_pickup_link"))
                    .setAcsUri(JsonUtils.getString(object, MEMBER_ACS_URI))
                    .setAcsParams(object.has(MEMBER_ACS_PARAMS) ?
                            JsonUtils.map(object.getAsJsonObject(MEMBER_ACS_PARAMS)) : null)
                    .setNextRetry(JsonUtils.getLong(object, MEMBER_NEXT_RETRY))
                    .setDigitalGoods(DigitalGoods.createFromJson(object.get("digital_goods")))
                    .createProcessPayment();
        }
    }
}
