package com.yandex.money.model.cps;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.misc.DigitalGoods;
import com.yandex.money.model.cps.misc.MoneySource;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ProcessPayment {

    private final Status status;
    private final Error error;
    private final String paymentId;
    private final BigDecimal balance;
    private final String invoiceId;
    private final String payer;
    private final String payee;
    private final BigDecimal creditAmount;
    private final String accountUnblockUri;
    private final String acsUri;
    private final Map<String, String> acsParams;
    private final Long nextRetry;
    private final DigitalGoods digitalGoods;

    private ProcessPayment(Status status, Error error, String paymentId, BigDecimal balance,
                           String invoiceId, String payer, String payee, BigDecimal creditAmount,
                           String accountUnblockUri, String acsUri, Map<String, String> acsParams,
                           Long nextRetry, DigitalGoods digitalGoods) {

        if (status == Status.SUCCESS && paymentId == null) {
            throw new NullPointerException("paymentId is null when status is success");
        }
        if (status == Status.EXT_AUTH_REQUIRED && acsUri == null) {
            throw new NullPointerException("acsUri is null when status is ext_auth_required");
        }
        this.status = status;
        this.error = error;
        this.paymentId = paymentId;
        this.balance = balance;
        this.invoiceId = invoiceId;
        this.payer = payer;
        this.payee = payee;
        this.creditAmount = creditAmount;
        this.accountUnblockUri = accountUnblockUri;
        this.acsUri = acsUri;
        this.acsParams = acsParams;
        this.nextRetry = nextRetry;
        this.digitalGoods = digitalGoods;
    }

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
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

    public String getAcsUri() {
        return acsUri;
    }

    public Map<String, String> getAcsParams() {
        return acsParams;
    }

    public Long getNextRetry() {
        return nextRetry;
    }

    public DigitalGoods getDigitalGoods() {
        return digitalGoods;
    }

    public enum Status {

        SUCCESS("success"),
        REFUSED("refused"),
        IN_PROGRESS("in_progress"),
        EXT_AUTH_REQUIRED("ext_auth_required"),
        UNKNOWN("unknown");

        private final String status;

        private Status(String status) {
            this.status = status;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.status.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    public static final class Request {

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

        public void setTestPayment(boolean testPayment) {
            this.testPayment = testPayment;
        }

        public void setTestCardAvailable(boolean testCardAvailable) {
            this.testCardAvailable = testCardAvailable;
        }

        public void setTestResult(TestResult testResult) {
            this.testResult = testResult;
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

        private final String result;

        private TestResult(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
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
                    creditAmount, accountUnblockUri, acsUri, acsParams, nextRetry, digitalGoods);
        }
    }

    private static final class Deserializer implements JsonDeserializer<ProcessPayment> {
        @Override
        public ProcessPayment deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new Builder()
                    .setStatus(Status.parse(JsonUtils.getMandatoryString(object, "status")))
                    .setError(Error.parse(JsonUtils.getString(object, "error")))
                    .setPaymentId(JsonUtils.getString(object, "payment_id"))
                    .setBalance(JsonUtils.getBigDecimal(object, "balance"))
                    .setInvoiceId(JsonUtils.getString(object, "invoice_id"))
                    .setPayer(JsonUtils.getString(object, "payer"))
                    .setPayee(JsonUtils.getString(object, "payee"))
                    .setCreditAmount(JsonUtils.getBigDecimal(object, "credit_amount"))
                    .setAccountUnblockUri(JsonUtils.getString(object, "account_unblock_uri"))
                    .setAcsUri(JsonUtils.getString(object, "acs_uri"))
                    .setAcsParams(JsonUtils.map(object.getAsJsonObject("acs_params")))
                    .setNextRetry(JsonUtils.getLong(object, "next_retry"))
                    .setDigitalGoods(DigitalGoods.createFromJson(object.get("digital_goods")))
                    .createProcessPayment();
        }
    }
}
