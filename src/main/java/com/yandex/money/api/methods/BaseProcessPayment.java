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

package com.yandex.money.api.methods;

import com.yandex.money.api.model.Error;
import com.yandex.money.api.utils.Constants;
import com.yandex.money.api.utils.Enums;
import com.yandex.money.api.utils.MillisecondsIn;

import java.util.Collections;
import java.util.Map;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Base class for all process payment operations.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseProcessPayment {

    public final Status status;
    public final Error error;
    public final String invoiceId;
    public final String acsUri;
    public final Map<String, String> acsParams;
    public final long nextRetry;

    /**
     * Constructor.
     */
    protected BaseProcessPayment(Builder builder) {
        checkNotNull(builder.status, "status");
        switch (builder.status) {
            case REFUSED:
                checkNotNull(builder.error, "error");
                break;
            case EXT_AUTH_REQUIRED:
                checkNotNull(builder.acsUri, "acsUri");
        }

        this.status = builder.status;
        this.error = builder.error;
        this.invoiceId = builder.invoiceId;
        this.acsUri = builder.acsUri;
        this.acsParams = Collections.unmodifiableMap(builder.acsParams);
        this.nextRetry = builder.nextRetry;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseProcessPayment that = (BaseProcessPayment) o;

        return nextRetry == that.nextRetry &&
                status == that.status &&
                error == that.error &&
                !(invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) &&
                !(acsUri != null ? !acsUri.equals(that.acsUri) : that.acsUri != null) &&
                acsParams.equals(that.acsParams);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (acsUri != null ? acsUri.hashCode() : 0);
        result = 31 * result + acsParams.hashCode();
        result = 31 * result + (int) (nextRetry ^ (nextRetry >>> 32));
        return result;
    }

    public enum Status implements Enums.WithCode<Status> {

        SUCCESS(Constants.Status.SUCCESS),
        REFUSED(Constants.Status.REFUSED),
        IN_PROGRESS(Constants.Status.IN_PROGRESS),
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

        public static Status parse(String code) {
            return Enums.parse(SUCCESS, code);
        }
    }

    public static abstract class Builder {

        private Status status;
        private Error error;
        private String invoiceId;
        private String acsUri;
        private Map<String, String> acsParams = Collections.emptyMap();
        private long nextRetry = 5 * MillisecondsIn.SECOND;

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
            checkNotNull(acsParams, "acsParams");
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
