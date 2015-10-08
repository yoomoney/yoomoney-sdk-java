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

import com.yandex.money.api.methods.params.PaymentParams;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.typeadapters.RequestExternalPaymentTypeAdapter;
import com.yandex.money.api.utils.Strings;

import java.util.Map;

/**
 * Context of an external payment.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class RequestExternalPayment extends BaseRequestPayment {

    /**
     * Title of payment.
     */
    public final String title;

    /**
     * Constructor.
     */
    private RequestExternalPayment(Builder builder) {
        super(builder);
        this.title = builder.title;
    }

    @Override
    public String toString() {
        return super.toString() + "RequestExternalPayment{" +
                "title='" + title + '\'' +
                '}';
    }

    /**
     * Requests context of external payment.
     */
    public static class Request extends PostRequest<RequestExternalPayment> {

        /**
         * Use static methods to create
         * {@link com.yandex.money.api.methods.RequestExternalPayment.Request}.
         */
        private Request(String instanceId, String patternId, Map<String, String> params) {
            super(RequestExternalPayment.class, RequestExternalPaymentTypeAdapter.getInstance());

            addParameter("instance_id", instanceId);
            addParameter("pattern_id", patternId);
            addParameters(params);
        }

        /**
         * Creates instance of payment's request for general purposes. In other words for payments
         * to a specific pattern_id with known parameters. Take a look at subclasses of
         * {@link PaymentParams} especially for p2p and phone-topup payments.
         *
         * @param instanceId application's instance id.
         * @param patternId pattern_id (p2p, phone-topup or shop).
         * @param params payment parameters.
         * @return new request instance.
         */
        public static Request newInstance(String instanceId, String patternId,
                                          Map<String, String> params) {
            Strings.checkNotNullAndNotEmpty(instanceId, "instanceId");
            Strings.checkNotNullAndNotEmpty(patternId, "patternId");

            if (params == null || params.isEmpty()) {
                throw new IllegalArgumentException("params is null or empty");
            }
            return new Request(instanceId, patternId, params);
        }

        /**
         * Convenience method for creating instance of payments.
         *
         * <p>
         * Note: the subset parameters of class {@link com.yandex.money.api.methods.params
         * .P2pParams} doesn't supported by now. Check out the documentation for additional
         * information.
         * </p>
         *
         * @param instanceId application's instance id.
         * @param paymentParams payment parameters wrapper.
         * @return new request instance.
         */
        public static Request newInstance(String instanceId, PaymentParams paymentParams) {
            if (paymentParams == null) {
                throw new IllegalArgumentException("paymentParams is null");
            }
            return Request.newInstance(instanceId, paymentParams.getPatternId(),
                    paymentParams.makeParams());
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/request-external-payment";
        }
    }

    public static final class Builder extends BaseRequestPayment.Builder {

        private String title;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        @Override
        public RequestExternalPayment create() {
            return new RequestExternalPayment(this);
        }
    }
}
