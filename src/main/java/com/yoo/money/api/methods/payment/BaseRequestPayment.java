/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.methods.payment;

import com.google.gson.annotations.SerializedName;
import com.yoo.money.api.model.Error;
import com.yoo.money.api.model.Fees;
import com.yoo.money.api.util.Constants;
import com.yoo.money.api.util.Enums;

import java.math.BigDecimal;

import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Base class for request payment operations.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public abstract class BaseRequestPayment {

    /**
     * Status of the request.
     */
    @SerializedName("status")
    public final Status status;

    /**
     * Error code if exists.
     */
    @SerializedName("error")
    public final Error error;

    /**
     * Request id.
     */
    @SerializedName("request_id")
    public final String requestId;

    /**
     * Contract amount.
     */
    @SerializedName("contract_amount")
    public final BigDecimal contractAmount;

    /**
     * Title of payment.
     */
    @SerializedName("title")
    public final String title;

    /**
     * Payment fees.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("fees")
    public final Fees fees;

    @SuppressWarnings("WeakerAccess")
    protected BaseRequestPayment(Builder builder) {
        status = checkNotNull(builder.status, "status");
        switch (status) {
            case SUCCESS:
                checkNotNull(builder.requestId, "requestId");
                checkNotNull(builder.contractAmount, "contractAmount");
                break;
            case REFUSED:
                checkNotNull(builder.error, "error");
                if(builder.error == Error.NOT_ENOUGH_FUNDS) {
                    checkNotNull(builder.contractAmount, "contractAmount");
                }
                break;
        }

        error = builder.error;
        requestId = builder.requestId;
        contractAmount = builder.contractAmount;
        title = builder.title;
        fees = builder.fees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseRequestPayment that = (BaseRequestPayment) o;

        if (status != that.status) return false;
        if (error != that.error) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (contractAmount != null ? !contractAmount.equals(that.contractAmount) : that.contractAmount != null)
            return false;
        //noinspection SimplifiableIfStatement
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return fees != null ? fees.equals(that.fees) : that.fees == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (contractAmount != null ? contractAmount.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (fees != null ? fees.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseRequestPayment{" +
                "status=" + status +
                ", error=" + error +
                ", requestId='" + requestId + '\'' +
                ", contractAmount=" + contractAmount +
                ", title='" + title + '\'' +
                ", fees=" + fees +
                '}';
    }

    public enum Status implements Enums.WithCode<Status> {

        @SerializedName(Constants.Status.SUCCESS)
        SUCCESS(Constants.Status.SUCCESS),
        @SerializedName(Constants.Status.REFUSED)
        REFUSED(Constants.Status.REFUSED),
        @SerializedName(Constants.Status.HOLD_FOR_PICKUP)
        HOLD_FOR_PICKUP(Constants.Status.HOLD_FOR_PICKUP);

        public final String code;

        Status(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Status[] getValues() {
            return values();
        }
    }

    public static abstract class Builder {

        Status status;
        String requestId;
        Error error;
        BigDecimal contractAmount;
        String title;
        Fees fees;

        public final Builder setContractAmount(BigDecimal contractAmount) {
            this.contractAmount = contractAmount;
            return this;
        }

        public final Builder setRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public final Builder setError(Error error) {
            this.error = error;
            return this;
        }

        public final Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setFees(Fees fees) {
            this.fees = fees;
            return this;
        }

        public abstract BaseRequestPayment create();
    }
}
