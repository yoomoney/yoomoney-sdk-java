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
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.methods.IncomingTransferRejectTypeAdapter;

import static com.yandex.money.api.utils.Common.checkNotEmpty;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Incoming transfer reject operation.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferReject {

    public final StatusInfo statusInfo;

    public IncomingTransferReject(StatusInfo statusInfo) {
        this.statusInfo = checkNotNull(statusInfo, "statusInfo");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncomingTransferReject that = (IncomingTransferReject) o;

        return statusInfo.equals(that.statusInfo);
    }

    @Override
    public int hashCode() {
        return statusInfo.hashCode();
    }

    @Override
    public String toString() {
        return "IncomingTransferReject{" +
                "statusInfo=" + statusInfo +
                '}';
    }

    /**
     * Rejects incoming transfer.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<IncomingTransferReject> {

        /**
         * Constructor.
         *
         * @param operationId rejecting operation id
         */
        public Request(String operationId) {
            super(IncomingTransferRejectTypeAdapter.getInstance());
            addParameter("operation_id", checkNotEmpty(operationId, "operationId"));
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/incoming-transfer-reject";
        }
    }
}
