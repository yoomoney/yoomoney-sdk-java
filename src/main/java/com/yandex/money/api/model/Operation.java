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

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Operation details.
 */
public class Operation {

    /**
     * Operation id.
     */
    public final String operationId;

    /**
     * Status of operation.
     */
    public final Status status;

    /**
     * Pattern id.
     */
    public final String patternId;

    /**
     * Direction of operation.
     */
    public final Direction direction;

    /**
     * Amount.
     */
    public final BigDecimal amount;

    /**
     * Received amount.
     */
    public final BigDecimal amountDue;

    /**
     * Fee.
     */
    public final BigDecimal fee;

    /**
     * Operation datetime.
     */
    public final DateTime datetime;

    /**
     * Title of operation.
     */
    public final String title;

    /**
     * Sender.
     */
    public final String sender;

    /**
     * Recipient.
     */
    public final String recipient;

    /**
     * Type of recipient identifier.
     */
    public final PayeeIdentifierType recipientType;

    /**
     * Message to recipient.
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
     * Protection code for operation.
     */
    public final String protectionCode;

    /**
     * Protection code expiration datetime.
     */
    public final DateTime expires;

    /**
     * Answer datetime of operation acceptance/revoke.
     */
    public final DateTime answerDatetime;

    /**
     * Label of operation.
     */
    public final String label;

    /**
     * Details of operation.
     */
    public final String details;

    /**
     * {@code true} if operation can be repeated.
     */
    public final boolean repeatable;

    /**
     * Payment parameters.
     */
    public final Map<String, String> paymentParameters;

    public final boolean favorite;

    /**
     * Type of operation.
     */
    public final Type type;

    /**
     * Digital goods.
     */
    public final DigitalGoods digitalGoods;

    /**
     * Use {@link com.yandex.money.api.model.Operation.Builder} instead.
     */
    protected Operation(Builder builder) {
        if (builder.paymentParameters == null) {
            throw new NullPointerException("payment parameters is null");
        }

        this.operationId = builder.operationId;
        this.status = builder.status;
        this.patternId = builder.patternId;
        this.direction = builder.direction;
        this.amount = builder.amount;
        this.amountDue = builder.amountDue;
        this.fee = builder.fee;
        this.datetime = builder.datetime;
        this.title = builder.title;
        this.sender = builder.sender;
        this.recipient = builder.recipient;
        this.recipientType = builder.recipientType;
        this.message = builder.message;
        this.comment = builder.comment;
        this.codepro = builder.codepro != null && builder.codepro;
        this.protectionCode = builder.protectionCode;
        this.expires = builder.expires;
        this.answerDatetime = builder.answerDatetime;
        this.label = builder.label;
        this.details = builder.details;
        this.repeatable = builder.repeatable != null && builder.repeatable;
        this.paymentParameters = Collections.unmodifiableMap(builder.paymentParameters);
        this.favorite = builder.favorite != null && builder.favorite;
        this.type = builder.type;
        this.digitalGoods = builder.digitalGoods;
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
     * Status of operation.
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
        NULL(null);

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static Status parse(String code) {
            if (code == null) {
                return NULL;
            }
            for (Status value : values()) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
            return NULL;
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
        NULL(null);

        public final String code;

        Type(String code) {
            this.code = code;
        }

        public static Type parse(String code) {
            for (Type value : values()) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
            return NULL;
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
        NULL(null);

        public final String code;

        Direction(String code) {
            this.code = code;
        }

        public static Direction parse(String code) {
            if (code == null) {
                return NULL;
            }
            for (Direction value : values()) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
            return NULL;
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

        public Operation create() {
            return new Operation(this);
        }
    }
}
