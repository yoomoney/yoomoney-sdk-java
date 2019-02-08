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

import com.google.gson.annotations.SerializedName;
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.time.DateTime;
import com.yandex.money.api.util.Enums;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Operation details.
 */
public class Operation implements Identifiable {

    /**
     * Operation id.
     */
    @SerializedName("operation_id")
    public final String operationId;

    /**
     * Status of operation.
     */
    @SerializedName("status")
    public final OperationStatus status;

    /**
     * Pattern id.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("pattern_id")
    public final String patternId;

    /**
     * Direction of operation.
     */
    @SerializedName("direction")
    public final Direction direction;

    /**
     * Amount.
     */
    @SerializedName("amount")
    public final BigDecimal amount;

    /**
     * Received amount.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("amount_due")
    public final BigDecimal amountDue;

    /**
     * Fee.
     */
    @SerializedName("fee")
    public final BigDecimal fee;

    /**
     * Operation datetime.
     */
    @SerializedName("datetime")
    public final DateTime datetime;

    /**
     * Title of operation.
     */
    @SerializedName("title")
    public final String title;

    /**
     * Sender.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("sender")
    public final String sender;

    /**
     * Recipient.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("recipient")
    public final String recipient;

    /**
     * Type of recipient identifier.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("recipient_type")
    public final PayeeIdentifierType recipientType;

    /**
     * Message to recipient.
     */
    @SerializedName("message")
    public final String message;

    /**
     * operation comment
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("comment")
    public final String comment;

    /**
     * {@code true} if operation is protected with a code
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("codepro")
    public final Boolean codepro;

    /**
     * Protection code for operation.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("protection_code")
    public final String protectionCode;

    /**
     * Protection code expiration datetime.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("expires")
    public final DateTime expires;

    /**
     * Answer datetime of operation acceptance/revoke.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("answer_datetime")
    public final DateTime answerDatetime;

    /**
     * Label of operation.
     */
    @SerializedName("label")
    public final String label;

    /**
     * Details of operation.
     */
    @SerializedName("details")
    public final String details;

    /**
     * {@code true} if operation can be repeated.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("repeatable")
    public final Boolean repeatable;

    /**
     * Payment parameters.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("payment_parameters")
    public final Map<String, String> paymentParameters;

    @SuppressWarnings("WeakerAccess")
    @SerializedName("favourite")
    public final Boolean favorite;

    /**
     * Type of operation.
     */
    @SerializedName("type")
    public final Type type;

    /**
     * Digital goods.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("digital_goods")
    public final DigitalGoods digitalGoods;

    /**
     * Id of categories
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("categories")
    public final List<Integer> categories;

    /**
     * Spending categories
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("spendingCategories")
    public final List<SpendingCategory> spendingCategories;

    /**
     * Type of showcase
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("showcase_format")
    public final ShowcaseReference.Format showcaseFormat;

    /**
     * Available operations
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("available_operations")
    public final List<AvailableOperation> availableOperations;

    /**
     * Operation currency code. ISO-4217 3-alpha currency symbol.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("amount_currency")
    public final String amountCurrency;

    /**
     * Exchange currency amount. The currency is always different from the currency of the account
     * for which the history is requested.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("exchange_amount")
    public final BigDecimal exchangeAmount;

    /**
     * Exchange currency code. ISO-4217 3-alpha currency symbol.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("exchange_amount_currency")
    public final String exchangeAmountCurrency;

    /**
     * Use {@link com.yandex.money.api.model.Operation.Builder} instead.
     */
    protected Operation(Builder builder) {
        operationId = builder.operationId;
        status = builder.status;
        type = builder.type;
        direction = builder.direction;
        title = builder.title;
        patternId = builder.patternId;
        amount = builder.amount;
        amountDue = builder.amountDue;
        fee = builder.fee;
        datetime = builder.datetime;
        sender = builder.sender;
        recipient = builder.recipient;
        recipientType = builder.recipientType;
        message = builder.message;
        comment = builder.comment;
        codepro = builder.codepro;
        protectionCode = builder.protectionCode;
        expires = builder.expires;
        answerDatetime = builder.answerDatetime;
        label = builder.label;
        details = builder.details;
        repeatable = builder.repeatable;
        paymentParameters = builder.paymentParameters != null ?
                Collections.unmodifiableMap(builder.paymentParameters) : null;
        favorite = builder.favorite;
        digitalGoods = builder.digitalGoods;
        categories = builder.categories != null ? Collections.unmodifiableList(builder.categories) : null;
        spendingCategories = builder.spendingCategories != null ? Collections.unmodifiableList(builder.spendingCategories) : null;
        showcaseFormat = builder.format;
        availableOperations = builder.availableOperations != null ?
                Collections.unmodifiableList(builder.availableOperations) : null;
        amountCurrency = builder.amountCurrency;
        exchangeAmount = builder.exchangeAmount;
        exchangeAmountCurrency = builder.exchangeAmountCurrency;
    }

    @Override
    public String getId() {
        return operationId;
    }

    public boolean isCodepro() {
        return codepro != null && codepro;
    }

    public boolean isRepeatable() {
        return repeatable != null && repeatable;
    }

    public boolean isFavorite() {
        return favorite != null && favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operation operation = (Operation) o;

        if (operationId != null ? !operationId.equals(operation.operationId) : operation.operationId != null)
            return false;
        if (status != operation.status) return false;
        if (patternId != null ? !patternId.equals(operation.patternId) : operation.patternId != null) return false;
        if (direction != operation.direction) return false;
        if (amount != null ? !amount.equals(operation.amount) : operation.amount != null) return false;
        if (amountDue != null ? !amountDue.equals(operation.amountDue) : operation.amountDue != null) return false;
        if (fee != null ? !fee.equals(operation.fee) : operation.fee != null) return false;
        if (datetime != null ? !datetime.equals(operation.datetime) : operation.datetime != null) return false;
        if (title != null ? !title.equals(operation.title) : operation.title != null) return false;
        if (sender != null ? !sender.equals(operation.sender) : operation.sender != null) return false;
        if (recipient != null ? !recipient.equals(operation.recipient) : operation.recipient != null) return false;
        if (recipientType != operation.recipientType) return false;
        if (message != null ? !message.equals(operation.message) : operation.message != null) return false;
        if (comment != null ? !comment.equals(operation.comment) : operation.comment != null) return false;
        if (codepro != null ? !codepro.equals(operation.codepro) : operation.codepro != null) return false;
        if (protectionCode != null ? !protectionCode.equals(operation.protectionCode) : operation.protectionCode != null)
            return false;
        if (expires != null ? !expires.equals(operation.expires) : operation.expires != null) return false;
        if (answerDatetime != null ? !answerDatetime.equals(operation.answerDatetime) : operation.answerDatetime != null)
            return false;
        if (label != null ? !label.equals(operation.label) : operation.label != null) return false;
        if (details != null ? !details.equals(operation.details) : operation.details != null) return false;
        if (repeatable != null ? !repeatable.equals(operation.repeatable) : operation.repeatable != null) return false;
        if (paymentParameters != null ? !paymentParameters.equals(operation.paymentParameters) : operation.paymentParameters != null)
            return false;
        if (favorite != null ? !favorite.equals(operation.favorite) : operation.favorite != null) return false;
        if (type != operation.type) return false;
        if (digitalGoods != null ? !digitalGoods.equals(operation.digitalGoods) : operation.digitalGoods != null)
            return false;
        if (categories != null ? !categories.equals(operation.categories) : operation.categories != null) return false;
        if (spendingCategories != null ? !spendingCategories.equals(operation.spendingCategories) : operation.spendingCategories != null) return false;
        //noinspection SimplifiableIfStatement
        if (showcaseFormat != operation.showcaseFormat) return false;
        if (amountCurrency != null ? !amountCurrency.equals(operation.amountCurrency) :
                operation.amountCurrency != null) return false;
        if (exchangeAmount != null ? !exchangeAmount.equals(operation.exchangeAmount) :
                operation.exchangeAmount != null) return false;
        if (exchangeAmountCurrency != null ? !exchangeAmountCurrency.equals(operation.exchangeAmountCurrency) :
                operation.exchangeAmountCurrency != null) return false;
        return availableOperations != null ? availableOperations.equals(operation.availableOperations) :
                operation.availableOperations == null;
    }

    @Override
    public int hashCode() {
        int result = operationId != null ? operationId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (patternId != null ? patternId.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (amountDue != null ? amountDue.hashCode() : 0);
        result = 31 * result + (fee != null ? fee.hashCode() : 0);
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (recipientType != null ? recipientType.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (codepro != null ? codepro.hashCode() : 0);
        result = 31 * result + (protectionCode != null ? protectionCode.hashCode() : 0);
        result = 31 * result + (expires != null ? expires.hashCode() : 0);
        result = 31 * result + (answerDatetime != null ? answerDatetime.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (repeatable != null ? repeatable.hashCode() : 0);
        result = 31 * result + (paymentParameters != null ? paymentParameters.hashCode() : 0);
        result = 31 * result + (favorite != null ? favorite.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (digitalGoods != null ? digitalGoods.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (spendingCategories != null ? spendingCategories.hashCode() : 0);
        result = 31 * result + (showcaseFormat != null ? showcaseFormat.hashCode() : 0);
        result = 31 * result + (availableOperations != null ? availableOperations.hashCode() : 0);
        result = 31 * result + (amountCurrency != null ? amountCurrency.hashCode() : 0);
        result = 31 * result + (exchangeAmount != null ? exchangeAmount.hashCode() : 0);
        result = 31 * result + (exchangeAmountCurrency != null ? exchangeAmountCurrency.hashCode() : 0);
        return result;
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
                ", categories=" + categories +
                ", spendingCategories=" + spendingCategories +
                ", showcaseFormat=" + showcaseFormat +
                ", availableOperations=" + availableOperations +
                ", amountCurrency=" + amountCurrency +
                ", exchangeAmount=" + exchangeAmount +
                ", exchangeAmountCurrency=" + exchangeAmountCurrency +
                '}';
    }

    /**
     * Type of operation.
     */
    public enum Type implements Enums.WithCode<Type> {
        /**
         * Payment to a shop.
         */
        @SerializedName("payment-shop")
        PAYMENT_SHOP("payment-shop"),
        /**
         * Outgoing transfer.
         */
        @SerializedName("outgoing-transfer")
        OUTGOING_TRANSFER("outgoing-transfer"),
        /**
         * Incoming transfer.
         */
        @SerializedName("incoming-transfer")
        INCOMING_TRANSFER("incoming-transfer"),
        /**
         * Incoming transfer with protection code.
         */
        @SerializedName("incoming-transfer-protected")
        INCOMING_TRANSFER_PROTECTED("incoming-transfer-protected"),
        /**
         * Deposition.
         */
        @SerializedName("deposition")
        DEPOSITION("deposition");

        public final String code;

        Type(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Type[] getValues() {
            return values();
        }
    }

    /**
     * Direction of operation.
     */
    public enum Direction implements Enums.WithCode<Direction> {
        /**
         * Incoming.
         */
        @SerializedName("in")
        INCOMING("in"),
        /**
         * Outgoing.
         */
        @SerializedName("out")
        OUTGOING("out");

        public final String code;

        Direction(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Direction[] getValues() {
            return values();
        }
    }

    /**
     * Types of available operations.
     *
     * These elements are for internal use only.
     * Use them carefully as they can be removed or changed.
     */
    public enum AvailableOperation implements Enums.WithCode<AvailableOperation> {

        @SerializedName("turn-on-reminder")
        TURN_ON_REMINDER("turn-on-reminder"),
        @SerializedName("turn-on-autopayment")
        TURN_ON_AUTOPAYMENT("turn-on-autopayment"),
        @SerializedName("repeat")
        REPEAT("repeat"),
        @SerializedName("add-to-favourites")
        ADD_TO_FAVOURITES("add-to-favourites");

        public final String code;

        AvailableOperation(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public AvailableOperation[] getValues() {
            return values();
        }
    }

    /**
     * Creates {@link com.yandex.money.api.model.Operation}.
     */
    public static class Builder {
        String operationId;
        OperationStatus status;
        String patternId;
        Direction direction;
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal amountDue;
        BigDecimal fee;
        DateTime datetime = DateTime.now();
        String title;
        String sender;
        String recipient;
        PayeeIdentifierType recipientType;
        String message;
        String comment;
        Boolean codepro;
        String protectionCode;
        DateTime expires;
        DateTime answerDatetime;
        String label;
        String details;
        Boolean repeatable;
        Map<String, String> paymentParameters;
        Boolean favorite;
        Type type;
        DigitalGoods digitalGoods;
        List<Integer> categories;
        List<SpendingCategory> spendingCategories;
        ShowcaseReference.Format format;
        List<AvailableOperation> availableOperations;
        String amountCurrency;
        BigDecimal exchangeAmount;
        String exchangeAmountCurrency;

        public Builder setOperationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder setStatus(OperationStatus status) {
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

        public Builder setCategories(List<Integer> categories) {
            this.categories = categories;
            return this;
        }

        public void setSpendingCategories(List<SpendingCategory> spendingCategories) {
            this.spendingCategories = spendingCategories;
        }

        public Builder setFormat(ShowcaseReference.Format format) {
            this.format = format;
            return this;
        }

        public Builder setAvailableOperations(List<AvailableOperation> operations) {
            this.availableOperations = operations;
            return this;
        }

        public Builder setAmountCurrency(String amountCurrency) {
            this.amountCurrency = amountCurrency;
            return this;
        }

        public Builder setExchangeAmount(BigDecimal exchangeAmount) {
            this.exchangeAmount = exchangeAmount;
            return this;
        }

        public Builder setExchangeAmountCurrency(String exchangeAmountCurrency) {
            this.exchangeAmountCurrency = exchangeAmountCurrency;
            return this;
        }

        public Operation create() {
            return new Operation(this);
        }
    }
}
