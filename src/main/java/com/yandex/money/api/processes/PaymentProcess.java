package com.yandex.money.api.processes;

import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.OAuth2Session;

/**
 * Payment process for authorized users.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class PaymentProcess extends BasePaymentProcess {

    /**
     * Constructor.
     *
     * @param session session to run the process on
     * @param parameterProvider parameter's provider
     */
    public PaymentProcess(OAuth2Session session, IPaymentProcess.ParameterProvider parameterProvider) {
        super(session, parameterProvider);
    }

    @Override
    protected MethodRequest<? extends BaseRequestPayment> createRequestPayment() {
        return new RequestPayment.Request(parameterProvider.getPatternId(),
                parameterProvider.getPaymentParameters());
    }

    @Override
    protected MethodRequest<? extends BaseProcessPayment> createProcessPayment() {
        return new ProcessPayment.Request(getRequestPayment().requestId,
                parameterProvider.getMoneySource(), parameterProvider.getCsc(),
                parameterProvider.getExtAuthSuccessUri(), parameterProvider.getExtAuthFailUri());
    }

    @Override
    protected MethodRequest<? extends BaseProcessPayment> createRepeatProcessPayment() {
        return new ProcessPayment.Request(getRequestPayment().requestId);
    }
}
