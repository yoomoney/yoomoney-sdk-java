package com.yandex.money.api.processes;

import com.squareup.okhttp.Call;
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
public abstract class BasePaymentProcess<RP extends BaseRequestPayment,
        PP extends BaseProcessPayment> implements IPaymentProcess {

    private static final long TIMEOUT = 3 * MillisecondsIn.SECOND;

    /**
     * Provides parameters for requests.
     */
    protected final ParameterProvider parameterProvider;

    private final OAuth2Session session;

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

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Call proceed(OAuth2Session.OnResponseReady<T> callback) throws Exception {
        switch (state) {
            case CREATED:
                return enqueueRequestPayment((OAuth2Session.OnResponseReady<RP>) callback);
            case STARTED:
                return enqueueProcessPayment((OAuth2Session.OnResponseReady<PP>) callback);
            case PROCESSING:
                return enqueueRepeatProcessPayment((OAuth2Session.OnResponseReady<PP>) callback);
            default:
                throw new IllegalStateException("payment process is broken");
        }
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

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Call repeat(OAuth2Session.OnResponseReady<T> callback) throws Exception {
        switch (state) {
            case STARTED:
                return enqueueRequestPayment((OAuth2Session.OnResponseReady<RP>) callback);
            case PROCESSING:
                return enqueueProcessPayment((OAuth2Session.OnResponseReady<PP>) callback);
            case COMPLETED:
                return enqueueRepeatProcessPayment((OAuth2Session.OnResponseReady<PP>) callback);
            default:
                throw new IllegalStateException("payment process is broken");
        }
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
    public final SavedState<RP, PP> getSavedState() {
        return new SavedState<>(requestPayment, processPayment, state);
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
    protected abstract MethodRequest<RP> createRequestPayment();

    /**
     * Creates process payment method.
     *
     * @return method request
     */
    protected abstract MethodRequest<PP> createProcessPayment();

    /**
     * Creates repeat process payment method.
     *
     * @return method request
     */
    protected abstract MethodRequest<PP> createRepeatProcessPayment();

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

    private void executeProcessPayment(final MethodRequest<PP> request) throws Exception {
        processPayment(new ProcessPaymentResolver<PP>() {
            @Override
            public PP getProcessPayment() throws Exception {
                return execute(request);
            }

            @Override
            public void onInProgress() throws Exception {
                executeProcessPayment(request);
            }
        });
    }

    private <T> T execute(MethodRequest<T> methodRequest) throws Exception {
        return session.execute(methodRequest);
    }

    private Call enqueueRequestPayment(final OAuth2Session.OnResponseReady<RP> callback)
            throws IOException {

        return enqueue(createRequestPayment(), new OAuth2Session.OnResponseReady<RP>() {
            @Override
            public void onFailure(Exception exception) {
                callback.onFailure(exception);
            }

            @Override
            public void onResponse(RP response) {
                requestPayment = response;
                state = State.STARTED;
                callback.onResponse(response);
            }
        });
    }

    private Call enqueueProcessPayment(OAuth2Session.OnResponseReady<PP> callback)
            throws IOException {
        return enqueueProcessPayment(createProcessPayment(), callback);
    }

    private Call enqueueRepeatProcessPayment(OAuth2Session.OnResponseReady<PP> callback)
            throws IOException {
        return enqueueProcessPayment(createRepeatProcessPayment(), callback);
    }

    private Call enqueueProcessPayment(final MethodRequest<PP> request,
                                       final OAuth2Session.OnResponseReady<PP> callback)
            throws IOException {

        return enqueue(request, new OAuth2Session.OnResponseReady<PP>() {
            @Override
            public void onFailure(Exception exception) {
                callback.onFailure(exception);
            }

            @Override
            public void onResponse(final PP response) {
                try {
                    if (processPayment(new ProcessPaymentResolver<PP>() {
                        @Override
                        public PP getProcessPayment() {
                            return response;
                        }

                        @Override
                        public void onInProgress() throws Exception {
                            enqueueProcessPayment(request, callback);
                        }
                    })) {
                        callback.onResponse(response);
                    }
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private <T> Call enqueue(MethodRequest<T> methodRequest,
                             OAuth2Session.OnResponseReady<T> callback) throws IOException {
        return session.enqueue(methodRequest, callback);
    }

    /**
     * Processes payment using {@link BasePaymentProcess.ProcessPaymentResolver} to resolve process
     * payment and repeat request as required.
     *
     * @param resolver the resolver
     * @return {@code true} if operation is complete
     */
    private boolean processPayment(ProcessPaymentResolver<PP> resolver) throws Exception {
        BaseProcessPayment.Status previousStatus = processPayment == null ? null :
                processPayment.status;
        processPayment = resolver.getProcessPayment();

        switch (processPayment.status) {
            case EXT_AUTH_REQUIRED:
                if (previousStatus != BaseProcessPayment.Status.EXT_AUTH_REQUIRED) {
                    state = State.PROCESSING;
                    return true;
                }
            case IN_PROGRESS:
                state = State.PROCESSING;
                Long nextRetry = processPayment.nextRetry;
                long timeout = nextRetry == null || nextRetry == 0L ? TIMEOUT : nextRetry;
                Threads.sleep(timeout);
                resolver.onInProgress();
                return false;
        }

        state = State.COMPLETED;
        return true;
    }

    private boolean isCompleted() {
        return state == State.COMPLETED;
    }

    /**
     * Saved state of payment process.
     */
    public static class SavedState<RP extends BaseRequestPayment, PP extends BaseProcessPayment> {

        private final RP requestPayment;
        private final PP processPayment;
        private final State state;

        /**
         * Constructor.
         *
         * @param requestPayment request payment
         * @param processPayment request payment
         * @param flags flags of saved state
         */
        public SavedState(RP requestPayment, PP processPayment, int flags) {
            this(requestPayment, processPayment, parseState(flags));
        }

        private SavedState(RP requestPayment, PP processPayment, State state) {
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

    private interface ProcessPaymentResolver<PP> {
        PP getProcessPayment() throws Exception;
        void onInProgress() throws Exception;
    }
}
