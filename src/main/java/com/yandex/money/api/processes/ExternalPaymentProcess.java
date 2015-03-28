package com.yandex.money.api.processes;

import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
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
public final class ExternalPaymentProcess extends BasePaymentProcess {

    private final String instanceId;

    private boolean requestToken;

    public ExternalPaymentProcess(OAuth2Session session, ParameterProvider parameterProvider,
                                  String instanceId) {
        super(session, parameterProvider);
        this.instanceId = instanceId;
    }

    /**
     * @param requestToken {@code true} if money source token is required
     */
    public void setRequestToken(boolean requestToken) {
        this.requestToken = requestToken;
    }

    @Override
    protected MethodRequest<? extends BaseRequestPayment> createRequestPayment() {
        ParameterProvider provider = getParameterProvider();
        return RequestExternalPayment.Request.newInstance(instanceId, provider.getPatternId(),
                provider.getPaymentParameters());
    }

    @Override
    protected MethodRequest<? extends BaseProcessPayment> createProcessPayment() {
        return createProcessExternalPayment();
    }

    @Override
    protected MethodRequest<? extends BaseProcessPayment> createRepeatProcessPayment() {
        return createProcessExternalPayment();
    }

    private ProcessExternalPayment.Request createProcessExternalPayment() {
        ParameterProvider provider = getParameterProvider();
        String requestId = getRequestPayment().requestId;
        String extAuthSuccessUri = provider.getExtAuthSuccessUri();
        String extAuthFailUri = provider.getExtAuthFailUri();
        MoneySource moneySource = provider.getMoneySource();
        String csc = provider.getCsc();
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
