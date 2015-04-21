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
import com.yandex.money.api.net.MethodResponse;

import java.util.Collections;
import java.util.Map;

/**
 * Base class for all process payment operations.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseProcessPayment implements MethodResponse {

    protected static final String MEMBER_STATUS = "status";
    protected static final String MEMBER_ERROR = "error";
    protected static final String MEMBER_ACS_URI = "acs_uri";
    protected static final String MEMBER_ACS_PARAMS = "acs_params";
    protected static final String MEMBER_NEXT_RETRY = "next_retry";

    public final Status status;
    public final Error error;
    public final String invoiceId;
    public final String acsUri;
    public final Map<String, String> acsParams;
    public final Long nextRetry; // TODO make it long

    /**
     * Constructor.
     *
     * @param status status of the operation
     * @param error error code
     * @param acsUri address for 3D Secure authorization
     * @param acsParams POST parameters for 3D Secure authorization (key-value pairs)
     * @param nextRetry recommended time interval between process payment requests
     */
    public BaseProcessPayment(Status status, Error error, String invoiceId, String acsUri,
                              Map<String, String> acsParams, Long nextRetry) {

        if (status == Status.EXT_AUTH_REQUIRED && acsUri == null) {
            throw new NullPointerException("acsUri is null when status is ext_auth_required");
        }
        if (acsParams == null) {
            throw new NullPointerException("acsParams is null");
        }
        this.status = status;
        this.error = error;
        this.invoiceId = invoiceId;
        this.acsUri = acsUri;
        this.acsParams = Collections.unmodifiableMap(acsParams);
        this.nextRetry = nextRetry;
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

    public enum Status {
        SUCCESS(CODE_SUCCESS),
        REFUSED(CODE_REFUSED),
        IN_PROGRESS(CODE_IN_PROGRESS),
        EXT_AUTH_REQUIRED(CODE_EXT_AUTH_REQUIRED),
        UNKNOWN(CODE_UNKNOWN);

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.code.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
}
