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

import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.utils.Threads;

/**
 * Base implementation for all payment processes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BasePaymentProcess<RP extends BaseRequestPayment,
        PP extends BaseProcessPayment> implements IPaymentProcess {

    private final OAuth2Session session;
    /**
     * Provides parameters for requests.
     */
    protected final ParameterProvider parameterProvider;
    private RP requestPayment;
    private PP processPayment;
    private State state;

    /**
     * Constructor.
     *
     * @param session session to run the process on
     * @param parameterProvider parameter's provider
     */
    public BasePaymentProcess(OAuth2Session session, ParameterProvider parameterProvider) {
        if (session == null) {
            throw new NullPointerException("session is null");
        }
        if (parameterProvider == null) {
            throw new NullPointerException("parameterProvider is null");
        }
        this.session = session;
        this.parameterProvider = parameterProvider;
        this.state = State.CREATED;
    }

    @Override
    public final boolean proceed() throws Exception {
        switch (state) {
            case CREATED:
                executeRequestPayment();
                break;
            case STARTED:
                executeProcessPayment();
                break;
            case PROCESSING:
                executeRepeatProcessPayment();
                break;
        }

        return isCompleted();
    }

    @Override
    public final boolean repeat() throws Exception {
        switch (state) {
            case STARTED:
                executeRequestPayment();
                break;
            case PROCESSING:
                executeProcessPayment();
                break;
            case COMPLETED:
                executeRepeatProcessPayment();
                break;
        }

        return isCompleted();
    }

    @Override
    public final void reset() {
        this.requestPayment = null;
        this.processPayment = null;
        this.state = State.CREATED;
    }

    /**
     * @return saved state for payment process
     */
    public SavedState<RP, PP> getSavedState() {
        return createSavedState(requestPayment, processPayment, state);
    }

    /**
     * Restores payment process from a saved state.
     *
     * @param savedState saved state
     */
    public final void restoreSavedState(SavedState<RP, PP> savedState) {
        if (savedState == null) {
            throw new NullPointerException("saved state is null");
        }
        this.requestPayment = savedState.getRequestPayment();
        this.processPayment = savedState.getProcessPayment();
        this.state = savedState.getState();
    }

    @Override
    public final BaseRequestPayment getRequestPayment() {
        return requestPayment;
    }

    @Override
    public final BaseProcessPayment getProcessPayment() {
        return processPayment;
    }

    /**
     * Sets access token to a session if required.
     *
     * @param accessToken access token
     */
    public final void setAccessToken(String accessToken) {
        session.setAccessToken(accessToken);
    }

    /**
     * Creates request payment method.
     *
     * @return method request
     */
    protected abstract ApiRequest<RP> createRequestPayment();

    /**
     * Creates process payment method.
     *
     * @return method request
     */
    protected abstract ApiRequest<PP> createProcessPayment();

    /**
     * Creates repeat process payment method.
     *
     * @return method request
     */
    protected abstract ApiRequest<PP> createRepeatProcessPayment();

    protected abstract SavedState<RP, PP> createSavedState(RP requestPayment, PP processPayment, State state);

    private void executeRequestPayment() throws Exception {
        requestPayment = execute(createRequestPayment());
        state = State.STARTED;
    }

    private void executeProcessPayment() throws Exception {
        executeProcessPayment(createProcessPayment());
    }

    private void executeRepeatProcessPayment() throws Exception {
        executeProcessPayment(createRepeatProcessPayment());
    }

    private void executeProcessPayment(final ApiRequest<PP> request) throws Exception {
        BaseProcessPayment.Status previousStatus = processPayment == null ? null :
                processPayment.status;
        processPayment = execute(request);

        switch (processPayment.status) {
            case EXT_AUTH_REQUIRED:
                if (previousStatus != BaseProcessPayment.Status.EXT_AUTH_REQUIRED) {
                    state = State.PROCESSING;
                    return;
                }
            case IN_PROGRESS:
                state = State.PROCESSING;
                Threads.sleep(processPayment.nextRetry);
                executeProcessPayment(request);
                return;
        }

        state = State.COMPLETED;
    }

    private <T> T execute(ApiRequest<T> apiRequest) throws Exception {
        return session.execute(apiRequest);
    }

    private boolean isCompleted() {
        return state == State.COMPLETED;
    }

    /**
     * State of payment process
     */
    enum State {
        /**
         * Indicates that payment process is created.
         */
        CREATED,
        /**
         * Indicates that payment process is started.
         */
        STARTED,
        /**
         * Indicates that payment process is in progress (not completed).
         */
        PROCESSING,
        /**
         * Indicates that payment process is completed.
         */
        COMPLETED
    }

    /**
     * Saved state of payment process.
     */
    public static class SavedState <RP extends BaseRequestPayment, PP extends BaseProcessPayment> {

        private final RP requestPayment;
        private final PP processPayment;
        private final State state;

        /**
         * Constructor.
         *
         * @param requestPayment request payment
         * @param processPayment process payment
         * @param flags flags of saved state
         */
        public SavedState(RP requestPayment, PP processPayment, int flags) {
            this(requestPayment, processPayment, parseState(flags));
        }

        /**
         * Constructor.
         *
         * @param requestPayment request payment
         * @param processPayment process payment
         * @param state state
         */
        protected SavedState(RP requestPayment, PP processPayment, State state) {
            if (state == null) {
                throw new NullPointerException("state is null");
            }
            this.state = state;

            switch (state) {
                case CREATED:
                    this.requestPayment = null;
                    this.processPayment = null;
                    break;
                case STARTED:
                    if (requestPayment == null) {
                        throw new NullPointerException("requestPayment is null");
                    }
                    this.requestPayment = requestPayment;
                    this.processPayment = null;
                    break;
                case PROCESSING:
                case COMPLETED:
                    if (requestPayment == null) {
                        throw new NullPointerException("requestPayment is null");
                    }
                    if (processPayment == null) {
                        throw new NullPointerException("processPayment is null");
                    }
                    this.requestPayment = requestPayment;
                    this.processPayment = processPayment;
                    break;
                default:
                    throw new IllegalArgumentException("unknown state: " + state);
            }
        }

        /**
         * @return request payment
         */
        public final RP getRequestPayment() {
            return requestPayment;
        }

        /**
         * @return process payment
         */
        public final PP getProcessPayment() {
            return processPayment;
        }

        /**
         * @return flags of payment process
         */
        public int getFlags() {
            return state.ordinal();
        }

        private static State parseState(int flags) {
            State[] values = State.values();
            int index = flags % 10;
            if (index >= values.length) {
                throw new IllegalArgumentException("invalid flags: " + flags);
            }
            return values[index];
        }

        /**
         * @return state
         */
        State getState() {
            return state;
        }
    }

    /**
     * @return state of payment process
     */
    final State getState() {
        return state;
    }
}
