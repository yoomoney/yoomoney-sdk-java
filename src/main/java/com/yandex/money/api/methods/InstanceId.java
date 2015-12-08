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
import com.yandex.money.api.model.SimpleStatus;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.typeadapters.InstanceIdTypeAdapter;

import static com.yandex.money.api.utils.Common.checkNotEmpty;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Instance ID result.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class InstanceId {

    public final SimpleStatus status;
    public final Error error;
    public final String instanceId;

    /**
     * Constructor.
     *
     * @param status status of an operation
     * @param error error code
     * @param instanceId instance id if success
     */
    public InstanceId(SimpleStatus status, Error error, String instanceId) {
        this.status = checkNotNull(status, "status");
        switch (this.status) {
            case SUCCESS:
                checkNotNull(instanceId, "instanceId");
                break;
            case REFUSED:
                checkNotNull(error, "error");
                break;
        }

        this.error = error;
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "InstanceId{" +
                "status=" + status +
                ", error=" + error +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceId that = (InstanceId) o;

        return status == that.status &&
                error == that.error &&
                !(instanceId != null ? !instanceId.equals(
                        that.instanceId) : that.instanceId != null);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
        return result;
    }

    public boolean isSuccess() {
        return status == SimpleStatus.SUCCESS;
    }

    /**
     * Request for a new instance id.
     */
    public static class Request extends PostRequest<InstanceId> {

        /**
         * Construct request using provided client ID.
         *
         * @param clientId client id of the application
         */
        public Request(String clientId) {
            super(InstanceIdTypeAdapter.getInstance());
            addParameter("client_id", checkNotEmpty(clientId, "clientId"));
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/instance-id";
        }
    }
}
