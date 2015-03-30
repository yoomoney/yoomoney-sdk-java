package com.yandex.money.api.processes;

import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.utils.Strings;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class ExternalPaymentProcess
        extends BasePaymentProcess<RequestExternalPayment, ProcessExternalPayment> {

    private final ParameterProvider parameterProvider;

    private String instanceId;

    public ExternalPaymentProcess(OAuth2Session session, ParameterProvider parameterProvider) {
        super(session, parameterProvider);
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
    protected MethodRequest<RequestExternalPayment> createRequestPayment() {
        return RequestExternalPayment.Request.newInstance(instanceId,
                parameterProvider.getPatternId(), parameterProvider.getPaymentParameters());
    }

    @Override
    protected MethodRequest<ProcessExternalPayment> createProcessPayment() {
        return createProcessExternalPayment();
    }

    @Override
    protected MethodRequest<ProcessExternalPayment> createRepeatProcessPayment() {
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

    /**
     * @see BasePaymentProcess.Callbacks
     */
    public interface Callbacks
            extends BasePaymentProcess.Callbacks<RequestExternalPayment, ProcessExternalPayment> {
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
