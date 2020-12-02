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

package com.yoo.money.api.methods.wallet;

import com.google.gson.annotations.SerializedName;
import com.yoo.money.api.model.Error;
import com.yoo.money.api.model.Operation;
import com.yoo.money.api.net.FirstApiRequest;
import com.yoo.money.api.net.providers.HostsProvider;

/**
 * Operation details result.
 */
public class OperationDetails extends Operation {

    @SerializedName("error")
    public final Error error;

    OperationDetails(Builder builder) {
        super(builder);
        error = builder.error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OperationDetails that = (OperationDetails) o;

        return error == that.error;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OperationDetails{" +
                "operationId='" + operationId + '\'' +
                ", error=" + error +
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

    public static class Builder extends Operation.Builder {

        Error error;

        public Builder setError(Error error) {
            this.error = error;
            return this;
        }

        public OperationDetails create() {
            return new OperationDetails(this);
        }
    }

    /**
     * Requests for specific operation details.
     * <p/>
     * Authorized session required.
     */
    public static class Request extends FirstApiRequest<OperationDetails> {

        /**
         * Constructor.
         *
         * @param operationId operation's id
         */
        public Request(String operationId) {
            super(OperationDetails.class);
            addParameter("operation_id", operationId);
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/operation-details";
        }
    }
}
