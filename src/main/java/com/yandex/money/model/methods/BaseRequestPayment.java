package com.yandex.money.model.methods;

import com.yandex.money.net.MethodResponse;
import com.yandex.money.utils.Error;

import java.math.BigDecimal;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseRequestPayment implements MethodResponse {

    protected static final String MEMBER_STATUS = "status";
    protected static final String MEMBER_ERROR = "error";
    protected static final String MEMBER_REQUEST_ID = "request_id";
    protected static final String MEMBER_CONTRACT_AMOUNT = "contract_amount";

    private final Status status;
    private final com.yandex.money.utils.Error error;
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
