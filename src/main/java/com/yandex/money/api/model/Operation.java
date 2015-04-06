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
import java.util.Collections;
import java.util.Map;

/**
 * Operation details.
 */
public class Operation {

    /**
     * operation id
     */
    public final String operationId;

    /**
     * status of operation
     */
    public final Status status;

    /**
     * pattern id
     */
    public final String patternId;

    /**
     * direction of operation
     */
    public final Direction direction;

    /**
     * amount
     */
    public final BigDecimal amount;

    /**
     * received amount
     */
    public final BigDecimal amountDue;

    /**
     * fee
     */
    public final BigDecimal fee;

    /**
     * operation datetime
     */
    public final DateTime datetime;

    /**
     * title of operation
     */
    public final String title;

    /**
     * sender
     */
    public final String sender;

    /**
     * recepient
     */
    public final String recipient;

    /**
     * type of recipient identifier
     */
    public final PayeeIdentifierType recipientType;

    /**
     * message to recepient
     */
    public final String message;

    /**
     * operation comment
     */
    public final String comment;

    /**
     * {@code true} if operation is protected with a code
     */
    public final boolean codepro;

    /**
     * protection code for operation
     */
    public final String protectionCode;

    /**
     * protection code expiration datetime
     */
    public final DateTime expires;

    /**
     * answer datetime of operation acceptance/revoke
     */
    public final DateTime answerDatetime;

    /**
     * label of operation
     */
    public final String label;

    /**
     * details of operation
     */
    public final String details;

    /**
     * {@code true} if operation can be repeated
     */
    public final boolean repeatable;

    /**
     * payment parameters
     */
    public final Map<String, String> paymentParameters;

    public final boolean favorite;

    /**
     * type of operation
     */
    public final Type type;

    /**
     * digital goods
     */
    public final DigitalGoods digitalGoods;

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
        this.codepro = codepro != null && codepro;
        this.protectionCode = protectionCode;
        this.expires = expires;
        this.answerDatetime = answerDatetime;
        this.label = label;
        this.details = details;
        this.repeatable = repeatable != null && repeatable;
        this.paymentParameters = paymentParameters == null ? null :
                Collections.unmodifiableMap(paymentParameters);
        this.favorite = favorite != null && favorite;
        this.type = type;
        this.digitalGoods = digitalGoods;
    }

    /**
     * Creates {@link com.yandex.money.api.model.Operation} from JSON.
     */
    public static Operation createFromJson(JsonElement element) {
        return buildGson().fromJson(element, Operation.class);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operationId='" + operationId + '\'' +
                ", status=" + status +
                ", patternId='" + patternId + '\'' +
                ", direction=" + direction +
                ", amount=" + amount +
                ", amountDue=" + amountDue +
                ", fee=" + fee +
                ", datetime=" + datetime +
                ", title='" + title + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", recipientType=" + recipientType +
                ", message='" + message + '\'' +
                ", comment='" + comment + '\'' +
                ", codepro=" + codepro +
                ", protectionCode='" + protectionCode + '\'' +
                ", expires=" + expires +
                ", answerDatetime=" + answerDatetime +
                ", label='" + label + '\'' +
                ", details='" + details + '\'' +
                ", repeatable=" + repeatable +
                ", paymentParameters=" + paymentParameters +
                ", favorite=" + favorite +
                ", type=" + type +
                ", digitalGoods=" + digitalGoods +
                '}';
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

        Status(String status) {
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

        Type(String type) {
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

        Direction(String direction) {
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
