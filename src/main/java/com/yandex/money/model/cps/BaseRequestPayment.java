package com.yandex.money.model.cps;

import com.yandex.money.net.MethodResponse;

import java.math.BigDecimal;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseRequestPayment implements MethodResponse {

    private final Status status;
    private final Error error;
    private final String requestId;
    private final BigDecimal contractAmount;

    protected BaseRequestPayment(Status status, Error error, String requestId,
                                 BigDecimal contractAmount) {

        if (status == Status.SUCCESS && requestId == null) {
            throw new IllegalArgumentException("requestId is null when status is success");
        }
        this.status = status;
        this.error = error;
        this.requestId = requestId;
        this.contractAmount = contractAmount;
    }

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public String getRequestId() {
        return requestId;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public enum Status {
        SUCCESS(CODE_SUCCESS),
        REFUSED(CODE_REFUSED),
        HOLD_FOR_PICKUP(CODE_HOLD_FOR_PICKUP),
        UNKNOWN(CODE_UNKNOWN);

        private final String status;

        private Status(String status) {
            this.status = status;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.status.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
}
