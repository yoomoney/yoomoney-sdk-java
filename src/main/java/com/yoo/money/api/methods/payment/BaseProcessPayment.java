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
import com.yoo.money.api.util.Constants;
import com.yoo.money.api.util.Enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Base class for all process payment operations.
 */
public abstract class BaseProcessPayment {

    @SerializedName("status")
    public final Status status;
    @SerializedName("error")
    public final Error error;
    @SuppressWarnings("WeakerAccess")
    @SerializedName("invoice_id")
    public final String invoiceId;
    @SuppressWarnings("WeakerAccess")
    @SerializedName("acs_uri")
    public final String acsUri;
    @SuppressWarnings("WeakerAccess")
    @SerializedName("acs_params")
    public final Map<String, String> acsParams;
    @SerializedName("next_retry")
    public final long nextRetry;

    /**
     * Constructor.
     */
    @SuppressWarnings("WeakerAccess")
    protected BaseProcessPayment(Builder builder) {
        status = checkNotNull(builder.status, "status");
        switch (status) {
            case REFUSED:
                checkNotNull(builder.error, "error");
                break;
            case EXT_AUTH_REQUIRED:
                checkNotNull(builder.acsUri, "acsUri");
        }

        error = builder.error;
        invoiceId = builder.invoiceId;
        acsUri = builder.acsUri;
        acsParams = builder.acsParams != null ? Collections.unmodifiableMap(builder.acsParams) : null;
        nextRetry = builder.nextRetry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseProcessPayment that = (BaseProcessPayment) o;

        if (nextRetry != that.nextRetry) return false;
        if (status != that.status) return false;
        if (error != that.error) return false;
        if (invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) return false;
        //noinspection SimplifiableIfStatement
        if (acsUri != null ? !acsUri.equals(that.acsUri) : that.acsUri != null) return false;
        return acsParams != null ? acsParams.equals(that.acsParams) : that.acsParams == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (acsUri != null ? acsUri.hashCode() : 0);
        result = 31 * result + (acsParams != null ? acsParams.hashCode() : 0);
        result = 31 * result + (int) (nextRetry ^ (nextRetry >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BaseProcessPayment{" +
                "status=" + status +
                ", error=" + error +
                ", invoiceId='" + invoiceId + '\'' +
                ", acsUri='" + acsUri + '\'' +
                ", acsParams=" + acsParams +
                ", nextRetry=" + nextRetry +
                '}';
    }

    public enum Status implements Enums.WithCode<Status> {

        @SerializedName(Constants.Status.SUCCESS)
        SUCCESS(Constants.Status.SUCCESS),
        @SerializedName(Constants.Status.REFUSED)
        REFUSED(Constants.Status.REFUSED),
        @SerializedName(Constants.Status.IN_PROGRESS)
        IN_PROGRESS(Constants.Status.IN_PROGRESS),
        @SerializedName(Constants.Status.EXT_AUTH_REQUIRED)
        EXT_AUTH_REQUIRED(Constants.Status.EXT_AUTH_REQUIRED);

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
        Error error;
        String invoiceId;
        String acsUri;
        Map<String, String> acsParams = Collections.emptyMap();
        long nextRetry = TimeUnit.SECONDS.toMillis(5L);

        public final Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public final Builder setError(Error error) {
            this.error = error;
            return this;
        }

        public final Builder setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public final Builder setAcsUri(String acsUri) {
            this.acsUri = acsUri;
            return this;
        }

        public final Builder setAcsParams(Map<String, String> acsParams) {
            this.acsParams = acsParams;
            return this;
        }

        public final Builder setNextRetry(long nextRetry) {
            this.nextRetry = nextRetry;
            return this;
        }

        public abstract BaseProcessPayment create();
    }
}
