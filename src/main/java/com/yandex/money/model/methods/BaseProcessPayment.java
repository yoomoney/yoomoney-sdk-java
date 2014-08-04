package com.yandex.money.model.methods;

import com.yandex.money.model.Error;
import com.yandex.money.net.MethodResponse;

import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseProcessPayment implements MethodResponse {

    protected static final String MEMBER_STATUS = "status";
    protected static final String MEMBER_ERROR = "error";
    protected static final String MEMBER_ACS_URI = "acs_uri";
    protected static final String MEMBER_ACS_PARAMS = "acs_params";
    protected static final String MEMBER_NEXT_RETRY = "next_retry";

    private final Status status;
    private final Error error;
    private final String acsUri;
    private final Map<String, String> acsParams;
    private final Long nextRetry;

    public BaseProcessPayment(Status status, Error error, String acsUri,
                              Map<String, String> acsParams, Long nextRetry) {

        if (status == Status.EXT_AUTH_REQUIRED && acsUri == null) {
            throw new NullPointerException("acsUri is null when status is ext_auth_required");
        }
        this.status = status;
        this.error = error;
        this.acsUri = acsUri;
        this.acsParams = acsParams;
        this.nextRetry = nextRetry;
    }

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public String getAcsUri() {
        return acsUri;
    }

    public Map<String, String> getAcsParams() {
        return acsParams;
    }

    public Long getNextRetry() {
        return nextRetry;
    }

    public enum Status {
        SUCCESS(CODE_SUCCESS),
        REFUSED(CODE_REFUSED),
        IN_PROGRESS(CODE_IN_PROGRESS),
        EXT_AUTH_REQUIRED(CODE_EXT_AUTH_REQUIRED),
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
