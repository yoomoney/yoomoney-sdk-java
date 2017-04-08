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
import com.yandex.money.api.model.SimpleResponse;
import com.yandex.money.api.model.SimpleStatus;
import com.yandex.money.api.net.FirstApiRequest;
import com.yandex.money.api.net.providers.HostsProvider;

import static com.yandex.money.api.util.Common.checkNotEmpty;

/**
 * Incoming transfer reject operation.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferReject extends SimpleResponse {

    public IncomingTransferReject(SimpleStatus status, Error error) {
        super(status, error);
    }

    @Override
    public String toString() {
        return "IncomingTransferReject{" +
                "status=" + status +
                ", error=" + error +
                '}';
    }

    /**
     * Rejects incoming transfer.
     * <p/>
     * Authorized session required.
     */
    public static final class Request extends FirstApiRequest<IncomingTransferReject> {

        /**
         * Constructor.
         *
         * @param operationId rejecting operation id
         */
        public Request(String operationId) {
            super(IncomingTransferReject.class);
            addParameter("operation_id", checkNotEmpty(operationId, "operationId"));
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/incoming-transfer-reject";
        }
    }
}
