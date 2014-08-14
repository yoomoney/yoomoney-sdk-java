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
                for (Map.Entry<String, String> parameter : paymentParameters.entrySet()) {
                    postRequestBodyBuffer.addParam(parameter.getKey(), parameter.getValue());
                }
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

        public Request setTestPayment(boolean testPayment) {
            this.testPayment = testPayment;
            return this;
        }

        public Request setTestCardAvailable(boolean testCardAvailable) {
            this.testCardAvailable = testCardAvailable;
            return this;
        }

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

        private void checkAmount(BigDecimal amount) {
            checkCommonAmount(amount, ABSOLUTE_MINIMUM_AMOUNT, "amount");
        }

        private void checkAmountDue(BigDecimal amountDue) {
            checkCommonAmount(amountDue, ABSOLUTE_MINIMUM_AMOUNT_DUE, "amountDue");
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

        public static class P2pBuilder {
            private String to;
            private BigDecimal amount;
            private BigDecimal amountDue;
            private String comment;
            private String message;
            private String label;
            private Boolean codepro;
            private Integer expirePeriod;

            public P2pBuilder setTo(String to) {
                this.to = to;
                return this;
            }

            public P2pBuilder setAmount(BigDecimal amount) {
                this.amount = amount;
                return this;
            }

            public P2pBuilder setAmountDue(BigDecimal amountDue) {
                this.amountDue = amountDue;
                return this;
            }

            public P2pBuilder setComment(String comment) {
                this.comment = comment;
                return this;
            }

            public P2pBuilder setMessage(String message) {
                this.message = message;
                return this;
            }

            public P2pBuilder setLabel(String label) {
                this.label = label;
                return this;
            }

            public P2pBuilder setCodepro(Boolean codepro) {
                this.codepro = codepro;
                return this;
            }

            public P2pBuilder setExpirePeriod(Integer expirePeriod) {
                this.expirePeriod = expirePeriod;
                return this;
            }

            public Request createRequest() {
                return new Request(to, amount, amountDue, comment, message, label, codepro,
                        expirePeriod);
            }
        }
    }

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

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setError(Error error) {
            this.error = error;
            return this;
        }

        public Builder setMoneySources(List<MoneySource> moneySources) {
            this.moneySources = moneySources;
            return this;
        }

        public Builder setCscRequired(Boolean cscRequired) {
            this.cscRequired = cscRequired;
            return this;
        }

        public Builder setRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder setContractAmount(BigDecimal contractAmount) {
            this.contractAmount = contractAmount;
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

        public RequestPayment createRequestPayment() {
            return new RequestPayment(status, error, moneySources, cscRequired, requestId,
                    contractAmount, balance, recipientAccountStatus, recipientAccountType,
                    protectionCode, accountUnblockUri, extActionUri);
        }
    }

    public static final class Deserializer implements JsonDeserializer<RequestPayment> {
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
                        cscRequired = JsonUtils.getMandatoryBoolean(object, "csc_required");
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
