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
import com.yoo.money.api.model.SimpleResponse;
import com.yoo.money.api.model.SimpleStatus;
import com.yoo.money.api.net.FirstApiRequest;
import com.yoo.money.api.net.providers.HostsProvider;

import static com.yoo.money.api.util.Common.checkNotEmpty;
import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Incoming transfer accept result.
 */
public class IncomingTransferAccept extends SimpleResponse {

    @SuppressWarnings("WeakerAccess")
    @SerializedName("protection_code_attempts_available")
    public final Integer protectionCodeAttemptsAvailable;
    @SuppressWarnings("WeakerAccess")
    @SerializedName("ext_action_uri")
    public final String extActionUri;

    /**
     * Constructor.
     *
     * @param status status of request
     * @param error error code if noth
     * @param protectionCodeAttemptsAvailable number of attempts available after invalid protection code submission
     * @param extActionUri address to perform external action for successful acceptance
     */
    public IncomingTransferAccept(SimpleStatus status, Error error, Integer protectionCodeAttemptsAvailable,
                                  String extActionUri) {

        super(status, error);
        switch (status) {
            case REFUSED:
                switch (error) {
                    case EXT_ACTION_REQUIRED:
                        checkNotNull(extActionUri, "extActionUri");
                        break;
                }
                break;
        }
        this.protectionCodeAttemptsAvailable = protectionCodeAttemptsAvailable;
        this.extActionUri = extActionUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IncomingTransferAccept that = (IncomingTransferAccept) o;

        //noinspection SimplifiableIfStatement
        if (protectionCodeAttemptsAvailable != null ? !protectionCodeAttemptsAvailable.equals(that.protectionCodeAttemptsAvailable) : that.protectionCodeAttemptsAvailable != null)
            return false;
        return extActionUri != null ? extActionUri.equals(that.extActionUri) : that.extActionUri == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (protectionCodeAttemptsAvailable != null ? protectionCodeAttemptsAvailable.hashCode() : 0);
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
     * Requests to perform {@link IncomingTransferAccept}.
     * <p/>
     * Authorized session required.
     */
    public static final class Request extends FirstApiRequest<IncomingTransferAccept> {

        /**
         * Constructor.
         *
         * @param operationId    unique operation id
         * @param protectionCode protection code if transfer is protected
         */
        public Request(String operationId, String protectionCode) {
            super(IncomingTransferAccept.class);
            addParameter("operation_id", checkNotEmpty(operationId, "operationId"));
            addParameter("protection_code", protectionCode);
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/incoming-transfer-accept";
        }
    }
}
