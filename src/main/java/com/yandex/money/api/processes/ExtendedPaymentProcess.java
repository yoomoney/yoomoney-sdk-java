package com.yandex.money.api.processes;

import com.squareup.okhttp.Call;
import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.model.Wallet;
import com.yandex.money.api.net.OAuth2Session;

/**
 * Combined payment process of {@link PaymentProcess} and {@link ExternalPaymentProcess}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class ExtendedPaymentProcess implements IPaymentProcess {

    private final PaymentProcess paymentProcess;
    private final ExternalPaymentProcess externalPaymentProcess;
    private final ParameterProvider parameterProvider;

    private PaymentContext paymentContext;
    private boolean mutablePaymentContext = true;

    /**
     * Constructor.
     *
     * @param session session to run the process on
     * @param parameterProvider parameter's provider
     */
    public ExtendedPaymentProcess(OAuth2Session session, ParameterProvider parameterProvider) {
        this.paymentProcess = new PaymentProcess(session, parameterProvider);
        this.externalPaymentProcess = new ExternalPaymentProcess(session, parameterProvider);
        this.parameterProvider = parameterProvider;
        this.paymentContext = session.isAuthorized() ? PaymentContext.PAYMENT :
                PaymentContext.EXTERNAL_PAYMENT;
    }

    @Override
    public boolean proceed() throws Exception {
        switchContextIfRequired();
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.proceed() :
                externalPaymentProcess.proceed();
    }

    @Override
    public <T> Call proceed(OAuth2Session.OnResponseReady<T> callback) throws Exception {
        switchContextIfRequired();
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.proceed(callback) :
                externalPaymentProcess.proceed(callback);
    }

    @Override
    public boolean repeat() throws Exception {
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.repeat() :
                externalPaymentProcess.repeat();
    }

    @Override
    public <T> Call repeat(OAuth2Session.OnResponseReady<T> callback) throws Exception {
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.repeat(callback) :
                externalPaymentProcess.repeat(callback);
    }

    @Override
    public void reset() {
        paymentProcess.reset();
        externalPaymentProcess.reset();
    }

    @Override
    public BaseRequestPayment getRequestPayment() {
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.getRequestPayment() :
                externalPaymentProcess.getRequestPayment();
    }

    @Override
    public BaseProcessPayment getProcessPayment() {
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.getProcessPayment() :
                externalPaymentProcess.getProcessPayment();
    }

    /**
     * Initializes {@link ExtendedPaymentProcess} with fixed payment context. Must be called right
     * after instance creation, before calling {@link #proceed()}.
     *
     * @param paymentContext payment context
     * @return this instance for chain purposes
     * @throws IllegalStateException if called when process's state is not {@code CREATED}
     */
    public ExtendedPaymentProcess initWithFixedPaymentContext(PaymentContext paymentContext) {
        if (paymentContext == null) {
            throw new NullPointerException("paymentContext is null");
        }
        if (getState() != BasePaymentProcess.State.CREATED) {
            throw new IllegalStateException("you should call initWithFixedPaymentContext() after " +
                    "constructor only");
        }
        this.paymentContext = paymentContext;
        this.mutablePaymentContext = false;
        return this;
    }

    /**
     * @return saved state
     */
    public SavedState getSavedState() {
        return new SavedState(paymentProcess.getSavedState(),
                externalPaymentProcess.getSavedState(), paymentContext, mutablePaymentContext);
    }

    /**
     * Restores process to its saved state
     *
     * @param savedState saved state
     */
    public void restoreSavedState(SavedState savedState) {
        if (savedState == null) {
            throw new NullPointerException("saved state is null");
        }
        paymentProcess.restoreSavedState(savedState.paymentProcessSavedState);
        externalPaymentProcess.restoreSavedState(savedState.externalPaymentProcessSavedState);
        paymentContext = savedState.paymentContext;
        mutablePaymentContext = savedState.mutablePaymentContext;
    }

    /**
     * @see {@link BasePaymentProcess#setAccessToken(String)}
     */
    public void setAccessToken(String accessToken) {
        paymentProcess.setAccessToken(accessToken);
        externalPaymentProcess.setAccessToken(accessToken);
    }

    /**
     * @see {@link ExternalPaymentProcess#setInstanceId(String)}
     */
    public void setInstanceId(String instanceId) {
        externalPaymentProcess.setInstanceId(instanceId);
    }

    /**
     * @see {@link ExternalPaymentProcess#setRequestToken(boolean)}
     */
    public void setRequestToken(boolean requestToken) {
        externalPaymentProcess.setRequestToken(requestToken);
    }

    private void switchContextIfRequired() {
        if (getState() == BasePaymentProcess.State.STARTED && mutablePaymentContext) {
            MoneySource moneySource = parameterProvider.getMoneySource();
            if (moneySource == null) {
                throw new NullPointerException("moneySource is null; not provided by " +
                        "ParameterProvider");
            }

            if (paymentContext == PaymentContext.PAYMENT && moneySource instanceof ExternalCard) {
                paymentContext = PaymentContext.EXTERNAL_PAYMENT;
            } else if (paymentContext == PaymentContext.EXTERNAL_PAYMENT &&
                    moneySource instanceof Wallet) {
                paymentContext = PaymentContext.PAYMENT;
            }
        }
    }

    private BasePaymentProcess.State getState() {
        return paymentContext == PaymentContext.PAYMENT ? paymentProcess.getState() :
                externalPaymentProcess.getState();
    }

    /**
     * Saved state of extended payment process.
     */
    public static final class SavedState {

        private final BasePaymentProcess.SavedState<RequestPayment, ProcessPayment> paymentProcessSavedState;
        private final BasePaymentProcess.SavedState<RequestExternalPayment, ProcessExternalPayment> externalPaymentProcessSavedState;
        private final PaymentContext paymentContext;
        private final boolean mutablePaymentContext;

        /**
         * Constructor.
         *
         * @param paymentProcessSavedState {@link PaymentProcess} saved state
         * @param externalPaymentProcessSavedState {@link ExtendedPaymentProcess} saved state
         * @param flags flags
         */
        public SavedState(BasePaymentProcess.SavedState<RequestPayment, ProcessPayment> paymentProcessSavedState,
                          BasePaymentProcess.SavedState<RequestExternalPayment, ProcessExternalPayment> externalPaymentProcessSavedState,
                          int flags) {

            this(paymentProcessSavedState, externalPaymentProcessSavedState, parseContext(flags),
                    parseMutablePaymentContext(flags));
        }

        private SavedState(BasePaymentProcess.SavedState<RequestPayment, ProcessPayment> paymentProcessSavedState,
                           BasePaymentProcess.SavedState<RequestExternalPayment, ProcessExternalPayment> externalPaymentProcessSavedState,
                           PaymentContext paymentContext, boolean mutablePaymentContext) {

            this.paymentProcessSavedState = paymentProcessSavedState;
            this.externalPaymentProcessSavedState = externalPaymentProcessSavedState;
            this.paymentContext = paymentContext;
            this.mutablePaymentContext = mutablePaymentContext;
        }

        /**
         * @return {@link PaymentProcess} saved state
         */
        public BasePaymentProcess.SavedState<RequestPayment, ProcessPayment> getPaymentProcessSavedState() {
            return paymentProcessSavedState;
        }

        /**
         * @return {@link ExtendedPaymentProcess} saved state
         */
        public BasePaymentProcess.SavedState<RequestExternalPayment, ProcessExternalPayment> getExternalPaymentProcessSavedState() {
            return externalPaymentProcessSavedState;
        }

        /**
         * @return flags for {@link ExtendedPaymentProcess}
         */
        public int getFlags() {
            return paymentContext.ordinal() + (mutablePaymentContext ? 10 : 0);
        }

        private static PaymentContext parseContext(int flags) {
            PaymentContext[] values = PaymentContext.values();
            int index = flags % 10;
            if (index >= values.length) {
                throw new IllegalArgumentException("invalid flags: " + flags);
            }
            return values[index];
        }

        private static boolean parseMutablePaymentContext(int flags) {
            int value = (flags / 10) % 10;
            if (value > 1) {
                throw new IllegalArgumentException("invalid flags: " + flags);
            }
            return value == 1;
        }
    }

    /**
     * Payment context.
     */
    public enum PaymentContext {
        /**
         * {@link PaymentProcess}
         */
        PAYMENT,

        /**
         * {@link ExternalPaymentProcess}
         */
        EXTERNAL_PAYMENT
    }
}
