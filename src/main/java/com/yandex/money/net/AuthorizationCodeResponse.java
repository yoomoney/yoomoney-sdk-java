package com.yandex.money.net;

import com.yandex.money.utils.UrlEncodedUtils;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AuthorizationCodeResponse {

    private final String code;
    private final Error error;
    private final String errorDescription;

    private AuthorizationCodeResponse(String code, Error error, String errorDescription) {
        this.code = code;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public static AuthorizationCodeResponse parse(String redirectUrl) throws URISyntaxException {
        Map<String, String> params = UrlEncodedUtils.parse(redirectUrl);
        return new AuthorizationCodeResponse(params.get("code"), Error.parse(params.get("error")),
                params.get("error_description"));
    }

    public String getCode() {
        return code;
    }

    public Error getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public enum Error {
        ACCESS_DENIED("access_denied"),
        INVALID_REQUEST("invalid_request"),
        INVALID_SCOPE("invalid_scope"),
        UNAUTHORIZED_CLIENT("unauthorized_client"),
        UNKNOWN("unknown");

        private final String code;

        private Error(String code) {
            this.code = code;
        }

        public static Error parse(String code) {
            if (code == null) {
                return null;
            }
            for (Error value : values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
}
