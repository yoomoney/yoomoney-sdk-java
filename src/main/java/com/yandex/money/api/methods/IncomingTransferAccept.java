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
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.typeadapters.IncomingTransferAcceptTypeAdapter;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Incoming transfer accept result.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferAccept implements MethodResponse {

    public final Status status;
    public final Error error;
    public final Integer protectionCodeAttemptsAvailable;
    public final String extActionUri;

    /**
     * Constructor.
     *
     * @param status                          status of an operation
     * @param error                           error code
     * @param protectionCodeAttemptsAvailable number of attempts available after invalid protection
     *                                        code submission
     * @param extActionUri                    address to perform external action for successful
     *                                        acceptance
     */
    public IncomingTransferAccept(Status status, Error error,
                                  Integer protectionCodeAttemptsAvailable, String extActionUri) {
        checkNotNull(status, "status");
        switch (status) {
            case REFUSED:
                checkNotNull(error, "error");
                if(error == Error.ILLEGAL_PARAM_PROTECTION_CODE) {
                    checkNotNull(protectionCodeAttemptsAvailable,
                            "protectionCodeAttemptsAvailable");
                }
                if(error == Error.EXT_ACTION_REQUIRED) {
                    checkNotNull(extActionUri, "extActionUri");
                }
        }
        this.status = status;
        this.error = error;
        this.protectionCodeAttemptsAvailable = protectionCodeAttemptsAvailable;
        this.extActionUri = extActionUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncomingTransferAccept that = (IncomingTransferAccept) o;

        return status == that.status &&
                error == that.error && !(protectionCodeAttemptsAvailable != null ?
                !protectionCodeAttemptsAvailable.equals(that.protectionCodeAttemptsAvailable) :
                    that.protectionCodeAttemptsAvailable != null) &&
                !(extActionUri != null ? !extActionUri.equals(that.extActionUri) :
                        that.extActionUri != null);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (protectionCodeAttemptsAvailable != null ?
                protectionCodeAttemptsAvailable.hashCode() : 0);
        result = 31 * result + (extActionUri != null ? extActionUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IncomingTransferAccept{" +
                "status=" + status +
                ", error=" + error +
                ", protectionCodeAttemptsAvailable=" + protectionCodeAttemptsAvailable +
                ", extActionUri='" + extActionUri + '\'' +
                '}';
    }

    /**
     * Status of {@link com.yandex.money.api.methods.IncomingTransferAccept}
     */
    public enum Status {
        /**
         * Successful.
         */
        SUCCESS(CODE_SUCCESS),
        /**
         * Operation refused.
         */
        REFUSED(CODE_REFUSED),
        /**
         * Unknown status.
         */
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

    /**
     * Requests to perform {@link com.yandex.money.api.methods.IncomingTransferAccept}.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<IncomingTransferAccept> {

        /**
         * Constructor.
         *
         * @param operationId    unique operation id
         * @param protectionCode protection code if transfer is protected
         */
        public Request(String operationId, String protectionCode) {
            super(IncomingTransferAccept.class, IncomingTransferAcceptTypeAdapter.getInstance());
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            addParameter("operation_id", operationId);
            addParameter("protection_code", protectionCode);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/incoming-transfer-accept";
        }
    }
}
