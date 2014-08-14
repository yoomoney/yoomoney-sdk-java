package com.yandex.money.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.JsonUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Operation details.
 */
public class Operation {

    private final String operationId;
    private final Status status;
    private final String patternId;
    private final Direction direction;
    private final BigDecimal amount;
    private final BigDecimal amountDue;
    private final BigDecimal fee;
    private final DateTime datetime;
    private final String title;
    private final String sender;
    private final String recipient;
    private final PayeeIdentifierType recipientType;
    private final String message;
    private final String comment;
    private final Boolean codepro;
    private final String protectionCode;
    private final DateTime expires;
    private final DateTime answerDatetime;
    private final String label;
    private final String details;
    private final Boolean repeatable;
    private final Map<String, String> paymentParameters;
    private final Boolean favorite;
    private final Type type;
    private final DigitalGoods digitalGoods;

    /**
     * Use {@link com.yandex.money.api.model.Operation.Builder} instead.
     */
    protected Operation(String operationId, Status status, String patternId, Direction direction,
                        BigDecimal amount, BigDecimal amountDue, BigDecimal fee, DateTime datetime,
                        String title, String sender, String recipient,
                        PayeeIdentifierType recipientType, String message, String comment,
                        Boolean codepro, String protectionCode, DateTime expires,
                        DateTime answerDatetime, String label, String details, Boolean repeatable,
                        Map<String, String> paymentParameters, Boolean favorite, Type type,
                        DigitalGoods digitalGoods) {

        this.operationId = operationId;
        this.status = status;
        this.patternId = patternId;
        this.direction = direction;
        this.amount = amount;
        this.amountDue = amountDue;
        this.fee = fee;
        this.datetime = datetime;
        this.title = title;
        this.sender = sender;
        this.recipient = recipient;
        this.recipientType = recipientType;
        this.message = message;
        this.comment = comment;
        this.codepro = codepro;
        this.protectionCode = protectionCode;
        this.expires = expires;
        this.answerDatetime = answerDatetime;
        this.label = label;
        this.details = details;
        this.repeatable = repeatable;
        this.paymentParameters = paymentParameters;
        this.favorite = favorite;
        this.type = type;
        this.digitalGoods = digitalGoods;
    }

    /**
     * Creates {@link com.yandex.money.api.model.Operation} from JSON.
     */
    public static Operation createFromJson(JsonElement element) {
        return buildGson().fromJson(element, Operation.class);
    }

    /**
     * @return operation id
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * @return status of operation
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return pattern id
     */
    public String getPatternId() {
        return patternId;
    }

    /**
     * @return direction of operation
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return received amount
     */
    public BigDecimal getAmountDue() {
        return amountDue;
    }

    /**
     * @return fee
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @return operation datetime
     */
    public DateTime getDatetime() {
        return datetime;
    }

    /**
     * @return title of operation
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return type of recipient identifier
     */
    public PayeeIdentifierType getRecipientType() {
        return recipientType;
    }

    /**
     * @return message to recipient
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return operation comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return {@code true} if operation is protected with a code
     */
    public boolean isProtected() {
        return codepro != null && codepro;
    }

    /**
     * @return protection code for operation
     */
    public String getProtectionCode() {
        return protectionCode;
    }

    /**
     * @return protection code expiration datetime
     */
    public DateTime getExpires() {
        return expires;
    }

    /**
     * @return answer datetime of operation acceptance/revoke
     */
    public DateTime getAnswerDatetime() {
        return answerDatetime;
    }

    /**
     * @return label of operation
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return details of operation
     */
    public String getDetails() {
        return details;
    }

    /**
     * @return {@code true} if operation can be repeated
     */
    public boolean isRepeatable() {
        return repeatable != null && repeatable;
    }

    /**
     * @return payment parameters
     */
    public Map<String, String> getPaymentParameters() {
        return paymentParameters;
    }

    public boolean isFavorite() {
        return favorite != null && favorite;
    }

    /**
     * @return type of operation
     */
    public Type getType() {
        return type;
    }

    /**
     * @return digital goods
     */
    public DigitalGoods getDigitalGoods() {
        return digitalGoods;
    }

    /**
     * Status of opeartion.
     */
    public enum Status {
        /**
         * Operation succeeded.
         */
        SUCCESS("success"),
        /**
         * Operation refused.
         */
        REFUSED("refused"),
        /**
         * Operation is in progress, e.g. P2P with protection code has not been received.
         */
        IN_PROGRESS("in_progress"),
        /**
         * Status of operation is unknown.
         */
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

    /**
     * Type of operation.
     */
    public enum Type {
        /**
         * Payment to a shop.
         */
        PAYMENT_SHOP("payment-shop"),
        /**
         * Outgoing transfer.
         */
        OUTGOING_TRANSFER("outgoing-transfer"),
        /**
         * Incoming transfer.
         */
        INCOMING_TRANSFER("incoming-transfer"),
        /**
         * Incoming transfer with protection code.
         */
        INCOMING_TRANSFER_PROTECTED("incoming-transfer-protected"),
        /**
         * Deposition.
         */
        DEPOSITION("deposition"),
        /**
         * Unknown.
         */
        UNKNOWN("unknown");

        private final String type;

        private Type(String type) {
            this.type = type;
        }

        public static Type parse(String type) {
            for (Type value : values()) {
                if (value.type.equals(type)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Direction of operation.
     */
    public enum Direction {
        /**
         * Incoming.
         */
        INCOMING("in"),
        /**
         * Outgoing.
         */
        OUTGOING("out"),
        /**
         * Unknown.
         */
        UNKNOWN("unknown");

        private final String direction;

        private Direction(String direction) {
            this.direction = direction;
        }

        public static Direction parse(String direction) {
            for (Direction value : values()) {
                if (value.direction.equals(direction)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Creates {@link com.yandex.money.api.model.Operation}.
     */
    public static class Builder {
        private String operationId;
        private Status status;
        private String patternId;
        private Direction direction;
        private BigDecimal amount;
        private BigDecimal amountDue;
        private BigDecimal fee;
        private DateTime datetime;
        private String title;
        private String sender;
        private String recipient;
        private PayeeIdentifierType recipientType;
        private String message;
        private String comment;
        private Boolean codepro;
        private String protectionCode;
        private DateTime expires;
        private DateTime answerDatetime;
        private String label;
        private String details;
        private Boolean repeatable;
        private Map<String, String> paymentParameters;
        private Boolean favorite;
        private Type type;
        private DigitalGoods digitalGoods;

        public Builder setOperationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setPatternId(String patternId) {
            this.patternId = patternId;
            return this;
        }

        public Builder setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder setAmountDue(BigDecimal amountDue) {
            this.amountDue = amountDue;
            return this;
        }

        public Builder setFee(BigDecimal fee) {
            this.fee = fee;
            return this;
        }

        public Builder setDatetime(DateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder setRecipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder setRecipientType(PayeeIdentifierType recipientType) {
            this.recipientType = recipientType;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setCodepro(Boolean codepro) {
            this.codepro = codepro;
            return this;
        }

        public Builder setProtectionCode(String protectionCode) {
            this.protectionCode = protectionCode;
            return this;
        }

        public Builder setExpires(DateTime expires) {
            this.expires = expires;
            return this;
        }

        public Builder setAnswerDatetime(DateTime answerDatetime) {
            this.answerDatetime = answerDatetime;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setRepeatable(Boolean repeatable) {
            this.repeatable = repeatable;
            return this;
        }

        public Builder setPaymentParameters(Map<String, String> paymentParameters) {
            this.paymentParameters = paymentParameters;
            return this;
        }

        public Builder setFavorite(Boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setDigitalGoods(DigitalGoods digitalGoods) {
            this.digitalGoods = digitalGoods;
            return this;
        }

        public Operation createOperation() {
            return new Operation(operationId, status, patternId, direction, amount, amountDue, fee,
                    datetime, title, sender, recipient, recipientType, message, comment, codepro,
                    protectionCode, expires, answerDatetime, label, details, repeatable,
                    paymentParameters, favorite, type, digitalGoods);
        }
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Operation.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<Operation> {

        @Override
        public Operation deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {

            final JsonObject o = json.getAsJsonObject();
            final String paymentParametersMember = "payment_parameters";
            return new Builder()
                    .setOperationId(JsonUtils.getMandatoryString(o, "operation_id"))
                    .setStatus(Status.parse(JsonUtils.getString(o, "status")))
                    .setDatetime(JsonUtils.getDateTime(o, "datetime"))
                    .setTitle(JsonUtils.getMandatoryString(o, "title"))
                    .setPatternId(JsonUtils.getString(o, "pattern_id"))
                    .setDirection(Direction.parse(
                            JsonUtils.getMandatoryString(o, "direction")))
                    .setAmount(JsonUtils.getBigDecimal(o, "amount"))
                    .setAmountDue(JsonUtils.getBigDecimal(o, "amount_due"))
                    .setFee(JsonUtils.getBigDecimal(o, "fee"))
                    .setLabel(JsonUtils.getString(o, "label"))
                    .setType(Type.parse(JsonUtils.getString(o, "type")))
                    .setSender(JsonUtils.getString(o, "sender"))
                    .setRecipient(JsonUtils.getString(o, "recipient"))
                    .setRecipientType(PayeeIdentifierType.parse(
                            JsonUtils.getString(o, "recipient_type")))
                    .setMessage(JsonUtils.getString(o, "message"))
                    .setComment(JsonUtils.getString(o, "comment"))
                    .setCodepro(JsonUtils.getBoolean(o, "codepro"))
                    .setProtectionCode(JsonUtils.getString(o, "protection_code"))
                    .setExpires(JsonUtils.getDateTime(o, "expires"))
                    .setAnswerDatetime(JsonUtils.getDateTime(o, "answer_datetime"))
                    .setDetails(JsonUtils.getString(o, "details"))
                    .setRepeatable(JsonUtils.getBoolean(o, "repeatable"))
                    .setPaymentParameters(o.has(paymentParametersMember) ?
                            JsonUtils.map(o.getAsJsonObject(paymentParametersMember)) : null)
                    .setFavorite(JsonUtils.getBoolean(o, "favourite"))
                    .setDigitalGoods(DigitalGoods.createFromJson(o.get("digital_goods")))
                    .createOperation();
        }
    }
}
