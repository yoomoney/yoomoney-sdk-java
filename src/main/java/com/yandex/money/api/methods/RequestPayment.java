package com.yandex.money.api.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.model.Wallet;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.PostRequestBodyBuffer;
import com.yandex.money.api.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Context of a payment.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class RequestPayment extends BaseRequestPayment {

    private final List<MoneySource> moneySources;
    private final Boolean cscRequired;
    private final BigDecimal balance;
    private final AccountStatus recipientAccountStatus;
    private final AccountType recipientAccountType;
    private final String protectionCode;
    private final String accountUnblockUri;
    private final String extActionUri;

    /**
     * Use builder to create an instance.
     */
    private RequestPayment(Status status, Error error, List<MoneySource> moneySources,
                           Boolean cscRequired, String requestId, BigDecimal contractAmount,
                           BigDecimal balance, AccountStatus recipientAccountStatus,
                           AccountType recipientAccountType, String protectionCode,
                           String accountUnblockUri, String extActionUri) {

        super(status, error, requestId, contractAmount);
        this.moneySources = moneySources;
        this.cscRequired = cscRequired;
        this.balance = balance;
        this.recipientAccountStatus = recipientAccountStatus;
        this.recipientAccountType = recipientAccountType;
        this.protectionCode = protectionCode;
        this.accountUnblockUri = accountUnblockUri;
        this.extActionUri = extActionUri;
    }

    @Override
    public String toString() {
        return "RequestPayment{" +
                "status=" + getStatus() +
                ", error=" + getError() +
                ", requestId='" + getRequestId() + '\'' +
                ", contractAmount=" + getContractAmount() +
                ", moneySources=" + moneySources +
                ", cscRequired=" + cscRequired +
                ", balance=" + balance +
                ", recipientAccountStatus=" + recipientAccountStatus +
                ", recipientAccountType=" + recipientAccountType +
                ", protectionCode='" + protectionCode + '\'' +
                ", accountUnblockUri='" + accountUnblockUri + '\'' +
                ", extActionUri='" + extActionUri + '\'' +
                '}';
    }

    public List<MoneySource> getMoneySources() {
        return moneySources;
    }

    public Boolean isCscRequired() {
        return cscRequired != null && cscRequired;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountStatus getRecipientAccountStatus() {
        return recipientAccountStatus;
    }

    public AccountType getRecipientAccountType() {
        return recipientAccountType;
    }

    public String getProtectionCode() {
        return protectionCode;
    }

    public String getAccountUnblockUri() {
        return accountUnblockUri;
    }

    public String getExtActionUri() {
        return extActionUri;
    }

    /**
     * Requests for a payment context.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request implements MethodRequest<RequestPayment> {

        private static final BigDecimal ABSOLUTE_MINIMUM_AMOUNT = new BigDecimal(0.02);
        private static final BigDecimal ABSOLUTE_MINIMUM_AMOUNT_DUE = new BigDecimal(0.01);

        private final String patternId;
        private final String to;
        private final BigDecimal amount;
        private final BigDecimal amountDue;
        private final String comment;
        private final String message;
        private final String label;
        private final Boolean codepro;
        private final Integer expirePeriod;
        private final Map<String, String> paymentParameters;
        private final String phoneNumber;

        private boolean testPayment;
        private boolean testCardAvailable;
        private TestResult testResult;

        /**
         * Requests for context of payment to a specific shop.
         *
         * @param patternId pattern id of a shop
         * @param paymentParameters payment parameters
         */
        public Request(String patternId, Map<String, String> paymentParameters) {
            checkNotNullAndNotEmpty(patternId, "patternId");
            if (paymentParameters == null) {
                throw new NullPointerException("paymentParameters is null");
            }
            this.patternId = patternId;
            this.paymentParameters = paymentParameters;
            this.to = null;
            this.amount = null;
            this.amountDue = null;
            this.comment = null;
            this.message = null;
            this.label = null;
            this.codepro = null;
            this.expirePeriod = null;
            this.phoneNumber = null;
        }

        /**
         * Requests for context of payment to top up a phone.
         *
         * @param phoneNumber phone number
         * @param amount amount
         */
        public Request(String phoneNumber, BigDecimal amount) {
            this.patternId = "phone-topup";
            checkNotNullAndNotEmpty(phoneNumber, "phoneNumber");
            this.phoneNumber = phoneNumber;
            if (amount == null) {
                throw new NullPointerException("amount is null");
            }
            if (amount.compareTo(ABSOLUTE_MINIMUM_AMOUNT) < 0) {
                throw new IllegalArgumentException("amount has illegal value " +
                        amount.toPlainString());
            }
            this.amount = amount;
            this.to = null;
            this.amountDue = null;
            this.comment = null;
            this.message = null;
            this.label = null;
            this.codepro = null;
            this.expirePeriod = null;
            this.paymentParameters = null;
        }

        /**
         * Requests for a context of payment to another user.
         *
         * @param to account number, phone number or email of a recipient
         * @param amount amount to pay
         * @param amountDue amount to receive
         * @param comment payment comment
         * @param message message to a recipient
         * @param label payment label
         * @param codepro {@code true} if payment should be protected whith a code
         * @param expirePeriod number of days during which a transfer can be received
         */
        private Request(String to, BigDecimal amount, BigDecimal amountDue, String comment,
                        String message, String label, Boolean codepro, Integer expirePeriod) {

            this.patternId = "p2p";
            checkNotNullAndNotEmpty(to, "to");
            this.to = to;
            if (amount == null) {
                if (amountDue == null) {
                    throw new NullPointerException("amount and amountDue is null");
                } else if (amountDue.compareTo(ABSOLUTE_MINIMUM_AMOUNT_DUE) < 0) {
                    throw new IllegalArgumentException("amountDue has illegal value: " +
                            amountDue.toPlainString());
                }
            } else {
                if (amountDue != null) {
                    throw new IllegalArgumentException("inconsistent values amount and amountDue");
                } else if (amount.compareTo(ABSOLUTE_MINIMUM_AMOUNT) < 0) {
                    throw new IllegalArgumentException("amount has illegal value: " +
                            amount.toPlainString());
                }
            }
            this.amount = amount;
            this.amountDue = amountDue;
            this.comment = comment;
            this.message = message;
            this.label = label;
            this.codepro = codepro;
            this.expirePeriod = expirePeriod;
            this.paymentParameters = null;
            this.phoneNumber = null;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/request-payment");
        }

        @Override
        public RequestPayment parseResponse(InputStream inputStream) {
            return createGson().fromJson(new InputStreamReader(inputStream), RequestPayment.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            PostRequestBodyBuffer postRequestBodyBuffer = new PostRequestBodyBuffer();
            if (paymentParameters != null) {
                postRequestBodyBuffer.addParams(paymentParameters);
            }
            return postRequestBodyBuffer
                    .addParam("pattern_id", patternId)
                    .addParamIfNotNull("phone-number", phoneNumber)
                    .addParamIfNotNull("to", to)
                    .addParamIfNotNull("amount", amount)
                    .addParamIfNotNull("amount_due", amountDue)
                    .addParamIfNotNull("comment", comment)
                    .addParamIfNotNull("message", message)
                    .addParamIfNotNull("label", label)
                    .addBooleanIfTrue("codepro", codepro)
                    .addParamIfNotNull("expire_period", expirePeriod);
        }

        /**
         * @param testPayment {@code true} if test payment
         */
        public Request setTestPayment(boolean testPayment) {
            this.testPayment = testPayment;
            return this;
        }

        /**
         * @param testCardAvailable {@code true} if test card is available
         */
        public Request setTestCardAvailable(boolean testCardAvailable) {
            this.testCardAvailable = testCardAvailable;
            return this;
        }

        /**
         * @param testResult requested result
         */
        public Request setTestResult(TestResult testResult) {
            this.testResult = testResult;
            return this;
        }

        private static Gson createGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(RequestPayment.class, new Deserializer())
                    .create();
        }

        private void checkNotNullAndNotEmpty(String value, String field) {
            if (Strings.isNullOrEmpty(value)) {
                throw new IllegalArgumentException(field + " is null or empty");
            }
        }

        private void checkCommonAmount(BigDecimal amount, BigDecimal absoluteMinimum, String field) {
            if (amount == null) {
                throw new NullPointerException("amount is null");
            }
            if (amount.compareTo(absoluteMinimum) < 0) {
                throw new IllegalArgumentException(field + " has illegal value " +
                        amount.toPlainString());
            }
        }

        /**
         * Creates P2P request payment.
         */
        public static class P2pBuilder {
            private String to;
            private BigDecimal amount;
            private BigDecimal amountDue;
            private String comment;
            private String message;
            private String label;
            private Boolean codepro;
            private Integer expirePeriod;

            /**
             * @param to account number, phone number or email of a recipient
             */
            public P2pBuilder setTo(String to) {
                this.to = to;
                return this;
            }

            /**
             * @param amount amount to pay
             */
            public P2pBuilder setAmount(BigDecimal amount) {
                this.amount = amount;
                return this;
            }

            /**
             * @param amountDue amount to receive
             */
            public P2pBuilder setAmountDue(BigDecimal amountDue) {
                this.amountDue = amountDue;
                return this;
            }

            /**
             * @param comment payment comment
             */
            public P2pBuilder setComment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * @param message message to a recipient
             */
            public P2pBuilder setMessage(String message) {
                this.message = message;
                return this;
            }

            /**
             * @param label payment label
             */
            public P2pBuilder setLabel(String label) {
                this.label = label;
                return this;
            }

            /**
             * @param codepro {@code true} if payment should be protected whith a code
             */
            public P2pBuilder setCodepro(Boolean codepro) {
                this.codepro = codepro;
                return this;
            }

            /**
             * @param expirePeriod number of days during which a transfer can be received
             */
            public P2pBuilder setExpirePeriod(Integer expirePeriod) {
                this.expirePeriod = expirePeriod;
                return this;
            }

            /**
             * @return {@link com.yandex.money.api.methods.RequestPayment.Request} for P2P
             * transaction
             */
            public Request createRequest() {
                return new Request(to, amount, amountDue, comment, message, label, codepro,
                        expirePeriod);
            }
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

        private final String result;

        private TestResult(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
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
            return new RequestPayment(status, error, moneySources, cscRequired, requestId,
                    contractAmount, balance, recipientAccountStatus, recipientAccountType,
                    protectionCode, accountUnblockUri, extActionUri);
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
