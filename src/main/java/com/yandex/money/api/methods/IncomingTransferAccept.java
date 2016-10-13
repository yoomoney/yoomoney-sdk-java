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

import com.yandex.money.api.model.StatusInfo;
import com.yandex.money.api.net.FirstApiRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.methods.IncomingTransferAcceptTypeAdapter;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Incoming transfer accept result.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferAccept {

    public final StatusInfo statusInfo;
    public final Integer protectionCodeAttemptsAvailable;
    public final String extActionUri;

    /**
     * Constructor.
     *
     * @param statusInfo status of operation
     * @param protectionCodeAttemptsAvailable number of attempts available after invalid protection code submission
     * @param extActionUri address to perform external action for successful acceptance
     */
    public IncomingTransferAccept(StatusInfo statusInfo, Integer protectionCodeAttemptsAvailable, String extActionUri) {
        this.statusInfo = checkNotNull(statusInfo, "statusInfo");
        switch (statusInfo.status) {
            case REFUSED:
                switch (statusInfo.error) {
                    case ILLEGAL_PARAM_PROTECTION_CODE:
                        checkNotNull(protectionCodeAttemptsAvailable, "protectionCodeAttemptsAvailable");
                        break;
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

        IncomingTransferAccept that = (IncomingTransferAccept) o;

        return statusInfo.equals(that.statusInfo) &&
                !(protectionCodeAttemptsAvailable != null ?
                        !protectionCodeAttemptsAvailable.equals(that.protectionCodeAttemptsAvailable) :
                        that.protectionCodeAttemptsAvailable != null) &&
                !(extActionUri != null ? !extActionUri.equals(that.extActionUri) : that.extActionUri != null);
    }

    @Override
    public int hashCode() {
        int result = statusInfo.hashCode();
        result = 31 * result + (protectionCodeAttemptsAvailable != null ? protectionCodeAttemptsAvailable.hashCode() : 0);
        result = 31 * result + (extActionUri != null ? extActionUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IncomingTransferAccept{" +
                "statusInfo=" + statusInfo +
                ", protectionCodeAttemptsAvailable=" + protectionCodeAttemptsAvailable +
                ", extActionUri='" + extActionUri + '\'' +
                '}';
    }

    /**
     * Requests to perform {@link com.yandex.money.api.methods.IncomingTransferAccept}.
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
            super(IncomingTransferAcceptTypeAdapter.getInstance());
            addParameter("operation_id", checkNotEmpty(operationId, "operationId"));
            addParameter("protection_code", protectionCode);
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/incoming-transfer-accept";
        }
    }
}
