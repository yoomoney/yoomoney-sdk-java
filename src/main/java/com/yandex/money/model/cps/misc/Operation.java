package com.yandex.money.model.cps.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.JsonUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;

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
    private final Type type;
    private final DigitalGoods digitalGoods;

    protected Operation(String operationId, Status status, String patternId, Direction direction,
                        BigDecimal amount, BigDecimal amountDue, BigDecimal fee, DateTime datetime,
                        String title, String sender, String recipient,
                        PayeeIdentifierType recipientType, String message, String comment,
                        boolean codepro, String protectionCode, DateTime expires,
                        DateTime answerDatetime, String label, String details, Type type,
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
        this.type = type;
        this.digitalGoods = digitalGoods;
    }

    public static Operation createFromJson(JsonElement element) {
        return buildGson().fromJson(element, Operation.class);
    }

    public String getOperationId() {
        return operationId;
    }

    public Status getStatus() {
        return status;
    }

    public String getPatternId() {
        return patternId;
    }

    public Direction getDirection() {
        return direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public String getTitle() {
        return title;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public PayeeIdentifierType getRecipientType() {
        return recipientType;
    }

    public String getMessage() {
        return message;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getCodepro() {
        return codepro;
    }

    public String getProtectionCode() {
        return protectionCode;
    }

    public DateTime getExpires() {
        return expires;
    }

    public DateTime getAnswerDatetime() {
        return answerDatetime;
    }

    public String getLabel() {
        return label;
    }

    public String getDetails() {
        return details;
    }

    public Type getType() {
        return type;
    }

    public DigitalGoods getDigitalGoods() {
        return digitalGoods;
    }

    public enum Status {
        SUCCESS("success"),
        REFUSED("refused"),
        IN_PROGRESS("in_progress"),
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

    public enum Type {
        PAYMENT_SHOP("payment-shop"),
        OUTGOING_TRANSFER("outgoing-transfer"),
        INCOMING_TRANSFER("incoming-transfer"),
        INCOMING_TRANSFER_PROTECTED("incoming-transfer-protected"),
        DEPOSITION("deposition"),
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

    public enum Direction {
        INCOMING("in"),
        OUTGOING("out"),
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
        private boolean codepro;
        private String protectionCode;
        private DateTime expires;
        private DateTime answerDatetime;
        private String label;
        private String details;
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

        public Builder setCodepro(boolean codepro) {
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
                    protectionCode, expires, answerDatetime, label, details, type, digitalGoods);
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
                    .setDigitalGoods(DigitalGoods.createFromJson(o.get("digital_goods")))
                    .createOperation();
        }
    }
}
