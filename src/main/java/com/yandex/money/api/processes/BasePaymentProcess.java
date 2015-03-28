package com.yandex.money.api.processes;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.utils.MillisecondsIn;
import com.yandex.money.api.utils.Threads;

import java.io.IOException;

/**
 * Base implementation for all payment processes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BasePaymentProcess implements IPaymentProcess {

    private static final long TIMEOUT = 3 * MillisecondsIn.SECOND;

    /**
     * Provides parameters for requests.
     */
    protected final ParameterProvider parameterProvider;

    private final OAuth2Session session;

    private BaseRequestPayment requestPayment;
    private BaseProcessPayment processPayment;
    private Callback callback;
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
    public final boolean proceed() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        switch (state) {
            case CREATED:
                executeRequestPayment();
                state = State.STARTED;
                break;
            case STARTED:
                state = executeProcessPayment();
                break;
            case PROCESSING:
                state = executeRepeatProcessPayment();
                break;
        }

        return isCompleted();
    }

    @Override
    public final boolean repeat() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

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
    public final SavedState getSavedState() {
        return new SavedState(requestPayment, processPayment, state);
    }

    /**
     * Restores payment process from a saved state.
     *
     * @param savedState saved state
     */
    public final void restoreSavedState(SavedState savedState) {
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

    @Override
    public final void setCallback(Callback callback) {
        this.callback = callback;
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
     * @return state of payment process
     */
    final State getState() {
        return state;
    }

    /**
     * Creates request payment method.
     *
     * @return method request
     */
    protected abstract MethodRequest<? extends BaseRequestPayment> createRequestPayment();

    /**
     * Creates process payment method.
     *
     * @return method request
     */
    protected abstract MethodRequest<? extends BaseProcessPayment> createProcessPayment();

    /**
     * Creates repeat process payment method.
     *
     * @return method request
     */
    protected abstract MethodRequest<? extends BaseProcessPayment> createRepeatProcessPayment();

    private void executeRequestPayment() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        requestPayment = execute(createRequestPayment());
        if (callback != null) {
            callback.onRequestPayment();
        }
    }

    private State executeProcessPayment() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {
        return executeProcessPayment(createProcessPayment());
    }

    private State executeRepeatProcessPayment() throws InvalidTokenException,
            InsufficientScopeException, InvalidRequestException, IOException {
        return executeProcessPayment(createRepeatProcessPayment());
    }

    private State executeProcessPayment(MethodRequest<? extends BaseProcessPayment> request)
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        BaseProcessPayment.Status previousStatus = processPayment == null ? null :
                processPayment.status;
        processPayment = execute(request);

        switch (processPayment.status) {
            case EXT_AUTH_REQUIRED:
                if (previousStatus != BaseProcessPayment.Status.EXT_AUTH_REQUIRED) {
                    if (callback != null) {
                        callback.onProcessPayment();
                    }
                    return State.PROCESSING;
                }
            case IN_PROGRESS:
                Long nextRetry = processPayment.nextRetry;
                long timeout = nextRetry == null || nextRetry == 0L ? TIMEOUT : nextRetry;
                Threads.sleep(timeout);
                executeProcessPayment(request);
                break;
            default:
                if (callback != null) {
                    callback.onProcessPayment();
                }
        }
        return State.COMPLETED;
    }

    private <T> T execute(MethodRequest<T> methodRequest) throws InvalidTokenException,
            InsufficientScopeException, InvalidRequestException, IOException {
        return session.execute(methodRequest);
    }

    private boolean isCompleted() {
        return state == State.COMPLETED;
    }

    /**
     * Saved state of payment process.
     */
    public static class SavedState {

        private final BaseRequestPayment requestPayment;
        private final BaseProcessPayment processPayment;
        private final State state;

        /**
         * Constructor.
         *
         * @param requestPayment request payment
         * @param processPayment request payment
         * @param flags flags of saved state
         */
        public SavedState(BaseRequestPayment requestPayment, BaseProcessPayment processPayment,
                          int flags) {
            this(requestPayment, processPayment, parseState(flags));
        }

        private SavedState(BaseRequestPayment requestPayment, BaseProcessPayment processPayment,
                          State state) {

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

        private static State parseState(int flags) {
            State[] values = State.values();
            int index = flags % 10;
            if (index >= values.length) {
                throw new IllegalArgumentException("invalid flags: " + flags);
            }
            return values[index];
        }

        /**
         * @return request payment
         */
        public final BaseRequestPayment getRequestPayment() {
            return requestPayment;
        }

        /**
         * @return process payment
         */
        public final BaseProcessPayment getProcessPayment() {
            return processPayment;
        }

        /**
         * @return flags of payment process
         */
        public int getFlags() {
            return state.ordinal();
        }

        /**
         * @return state
         */
        State getState() {
            return state;
        }
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
}
