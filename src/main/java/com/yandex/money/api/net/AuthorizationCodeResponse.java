package com.yandex.money.api.net;

import com.yandex.money.api.model.Error;
import com.yandex.money.api.utils.UrlEncodedUtils;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AuthorizationCodeResponse {

    private final String code;
    private final Error error;
    private final String errorDescription;

    protected AuthorizationCodeResponse(String code, Error error, String errorDescription) {
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
}
