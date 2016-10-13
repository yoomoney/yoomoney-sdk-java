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

import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.util.Strings;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class ExternalPaymentProcess
        extends BasePaymentProcess<RequestExternalPayment, ProcessExternalPayment> {

    private final ParameterProvider parameterProvider;

    private String instanceId;

    public ExternalPaymentProcess(ApiClient client, ParameterProvider parameterProvider) {
        super(client, parameterProvider);
        this.parameterProvider = parameterProvider;
    }

    @Override
    public SavedState getSavedState() {
        return (SavedState) super.getSavedState();
    }

    /**
     * @param instanceId instance id
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    protected ApiRequest<RequestExternalPayment> createRequestPayment() {
        return RequestExternalPayment.Request.newInstance(instanceId,
                parameterProvider.getPatternId(), parameterProvider.getPaymentParameters());
    }

    @Override
    protected ApiRequest<ProcessExternalPayment> createProcessPayment() {
        return createProcessExternalPayment();
    }

    @Override
    protected ApiRequest<ProcessExternalPayment> createRepeatProcessPayment() {
        return createProcessExternalPayment();
    }

    @Override
    protected SavedState createSavedState(RequestExternalPayment requestPayment,
                                          ProcessExternalPayment processPayment, State state) {
        return new SavedState(requestPayment, processPayment, state);
    }

    private ProcessExternalPayment.Request createProcessExternalPayment() {
        String requestId = getRequestPayment().requestId;
        String extAuthSuccessUri = parameterProvider.getExtAuthSuccessUri();
        String extAuthFailUri = parameterProvider.getExtAuthFailUri();
        MoneySource moneySource = parameterProvider.getMoneySource();
        String csc = parameterProvider.getCsc();
        if (moneySource == null || !(moneySource instanceof ExternalCard) ||
                Strings.isNullOrEmpty(csc)) {
            return new ProcessExternalPayment.Request(instanceId, requestId, extAuthSuccessUri,
                    extAuthFailUri, parameterProvider.isRequestToken());
        } else {
            return new ProcessExternalPayment.Request(instanceId, requestId, extAuthSuccessUri,
                    extAuthFailUri, (ExternalCard) moneySource, csc);
        }
    }

    /**
     * Extends {@link BasePaymentProcess.ParameterProvider} interface providing additional
     * parameters for
     */
    public interface ParameterProvider extends BasePaymentProcess.ParameterProvider {
        /**
         * @return {@code true} if should request token for money source
         */
        boolean isRequestToken();
    }

    public static final class SavedState
            extends BasePaymentProcess.SavedState<RequestExternalPayment, ProcessExternalPayment> {

        public SavedState(RequestExternalPayment requestPayment,
                          ProcessExternalPayment processPayment, int flags) {
            super(requestPayment, processPayment, flags);
        }

        SavedState(RequestExternalPayment requestPayment, ProcessExternalPayment processPayment,
                   State state) {
            super(requestPayment, processPayment, state);
        }
    }
}
