package com.yandex.money.model.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.Error;
import com.yandex.money.model.methods.misc.DigitalGoods;
import com.yandex.money.model.methods.misc.MoneySource;
import com.yandex.money.net.HostsProvider;
import com.yandex.money.net.MethodRequest;
import com.yandex.money.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ProcessPayment extends BaseProcessPayment {

    private final String paymentId;
    private final BigDecimal balance;
    private final String invoiceId;
    private final String payer;
    private final String payee;
    private final BigDecimal creditAmount;
    private final String accountUnblockUri;
    private final String payeeUid;
    private final String holdForPickupLink;
    private final DigitalGoods digitalGoods;

    private ProcessPayment(Status status, Error error, String paymentId, BigDecimal balance,
                           String invoiceId, String payer, String payee, BigDecimal creditAmount,
                           String accountUnblockUri, String payeeUid, String holdForPickupLink,
                           String acsUri, Map<String, String> acsParams, Long nextRetry,
                           DigitalGoods digitalGoods) {

        super(status, error, acsUri, acsParams, nextRetry);
        if (status == Status.SUCCESS && paymentId == null) {
            throw new NullPointerException("paymentId is null when status is success");
        }
        this.paymentId = paymentId;
        this.balance = balance;
        this.invoiceId = invoiceId;
        this.payer = payer;
        this.payee = payee;
        this.creditAmount = creditAmount;
        this.accountUnblockUri = accountUnblockUri;
        this.payeeUid = payeeUid;
        this.holdForPickupLink = holdForPickupLink;
        this.digitalGoods = digitalGoods;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getPayer() {
        return payer;
    }

    public String getPayee() {
        return payee;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public String getAccountUnblockUri() {
        return accountUnblockUri;
    }

    public String getPayeeUid() {
        return payeeUid;
    }

    public String getHoldForPickupLink() {
        return holdForPickupLink;
    }

    public DigitalGoods getDigitalGoods() {
        return digitalGoods;
    }

    public static final class Request implements MethodRequest<ProcessPayment> {

        private final String requestId;
        private final MoneySource moneySource;
        private final String csc;
        private final String extAuthSuccessUri;
        private final String extAuthFailUri;

        private boolean testPayment;
        private boolean testCardAvailable;
        private TestResult testResult;

        public Request(String requestId) {
            this(requestId, null, null, null, null);
        }

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
            return new URL(hostsProvider.getMoneyApi() + "/request-payment");
        }

        @Override
        public ProcessPayment parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), ProcessPayment.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            PostRequestBodyBuffer postRequestBodyBuffer = new PostRequestBodyBuffer();
            if (moneySource != null) {
                postRequestBodyBuffer.addParam("money_source", moneySource.getId());
            }
            if (testResult != null) {
                postRequestBodyBuffer.addParam("test_result", testResult.getCode());
            }
            return postRequestBodyBuffer
                    .addParam("request_id", requestId)
                    .addParamIfNotNull("csc", csc)
                    .addParamIfNotNull("ext_auth_success_uri", extAuthSuccessUri)
                    .addParamIfNotNull("ext_auth_fail_uri", extAuthFailUri)
                    .addParamIfNotNull("test_payment", testPayment)
                    .addParamIfNotNull("test_card", testCardAvailable);
        }

        public void setTestPayment(boolean testPayment) {
            this.testPayment = testPayment;
        }

        public void setTestCardAvailable(boolean testCardAvailable) {
            this.testCardAvailable = testCardAvailable;
        }

        public void setTestResult(TestResult testResult) {
            this.testResult = testResult;
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(ProcessPayment.class, new Deserializer())
                    .create();
        }
    }

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

        private final String code;

        private TestResult(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

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

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setError(Error error) {
            this.error = error;
            return this;
        }

        public Builder setPaymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
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

        public Builder setAcsUri(String acsUri) {
            this.acsUri = acsUri;
            return this;
        }

        public Builder setAcsParams(Map<String, String> acsParams) {
            this.acsParams = acsParams;
            return this;
        }

        public Builder setNextRetry(Long nextRetry) {
            this.nextRetry = nextRetry;
            return this;
        }

        public Builder setDigitalGoods(DigitalGoods digitalGoods) {
            this.digitalGoods = digitalGoods;
            return this;
        }

        public ProcessPayment createProcessPayment() {
            return new ProcessPayment(status, error, paymentId, balance, invoiceId, payer, payee,
                    creditAmount, accountUnblockUri, payeeUid, holdForPickupLink, acsUri, acsParams,
                    nextRetry, digitalGoods);
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
