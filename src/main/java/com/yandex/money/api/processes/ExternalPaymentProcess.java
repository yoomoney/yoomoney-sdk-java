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

    private String instanceId;
    private boolean requestToken;

    public ExternalPaymentProcess(OAuth2Session session, ParameterProvider parameterProvider) {
        super(session, parameterProvider);
    }

    /**
     * @param instanceId instance id
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @param requestToken {@code true} if money source token is required
     */
    public void setRequestToken(boolean requestToken) {
        this.requestToken = requestToken;
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

    private ProcessExternalPayment.Request createProcessExternalPayment() {
        String requestId = getRequestPayment().requestId;
        String extAuthSuccessUri = parameterProvider.getExtAuthSuccessUri();
        String extAuthFailUri = parameterProvider.getExtAuthFailUri();
        MoneySource moneySource = parameterProvider.getMoneySource();
        String csc = parameterProvider.getCsc();
        if (moneySource == null || !(moneySource instanceof ExternalCard) ||
                Strings.isNullOrEmpty(csc)) {
            return new ProcessExternalPayment.Request(instanceId, requestId, extAuthSuccessUri,
                    extAuthFailUri, requestToken);
        } else {
            return new ProcessExternalPayment.Request(instanceId, requestId, extAuthSuccessUri,
                    extAuthFailUri, (ExternalCard) moneySource, csc);
        }
    }
}
