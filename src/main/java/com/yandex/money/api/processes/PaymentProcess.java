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

package com.yandex.money.api.processes;

import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.net.ApiClient;
import com.yandex.money.api.net.ApiRequest;

/**
 * Payment process for authorized users.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class PaymentProcess extends BasePaymentProcess<RequestPayment, ProcessPayment> {

    /**
     * Constructor.
     *
     * @param client client to run the process on
     * @param parameterProvider parameter's provider
     */
    public PaymentProcess(ApiClient client, IPaymentProcess.ParameterProvider parameterProvider) {
        super(client, parameterProvider);
    }

    @Override
    public SavedState getSavedState() {
        return (SavedState) super.getSavedState();
    }

    @Override
    protected ApiRequest<RequestPayment> createRequestPayment() {
        return RequestPayment.Request.newInstance(parameterProvider.getPatternId(),
                parameterProvider.getPaymentParameters());
    }

    @Override
    protected ApiRequest<ProcessPayment> createProcessPayment() {
        return new ProcessPayment.Request(getRequestPayment().requestId,
                parameterProvider.getMoneySource(), parameterProvider.getCsc(),
                parameterProvider.getExtAuthSuccessUri(), parameterProvider.getExtAuthFailUri());
    }

    @Override
    protected ApiRequest<ProcessPayment> createRepeatProcessPayment() {
        return new ProcessPayment.Request(getRequestPayment().requestId);
    }

    @Override
    protected SavedState createSavedState(RequestPayment requestPayment,
                                          ProcessPayment processPayment, State state) {
        return new SavedState(requestPayment, processPayment, state);
    }

    public static final class SavedState
            extends BasePaymentProcess.SavedState<RequestPayment, ProcessPayment> {

        public SavedState(RequestPayment requestPayment, ProcessPayment processPayment, int flags) {
            super(requestPayment, processPayment, flags);
        }

        SavedState(RequestPayment requestPayment, ProcessPayment processPayment, State state) {
            super(requestPayment, processPayment, state);
        }
    }
}
