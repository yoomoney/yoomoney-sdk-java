package com.yandex.money.api.net;

import com.yandex.money.api.model.Error;
import com.yandex.money.api.utils.UrlEncodedUtils;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * This is a convenience class to parse OAuth2 response after user authentication.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 * @see com.yandex.money.api.net.OAuth2Authorization
 */
public class AuthorizationCodeResponse {

    /**
     * temporary access token to request OAuth2 access token
     * @see com.yandex.money.api.methods.Token
     */
    public final String code;

    /**
     * error code
     */
    public final Error error;

    /**
     * human understandable error description
     */
    public final String errorDescription;

    protected AuthorizationCodeResponse(String code, Error error, String errorDescription) {
        this.code = code;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    /**
     * Parses redirect URL after OAuth2 authorization and retrieves its parameters.
     *
     * @param redirectUrl redirect url
     * @return response object
     * @throws URISyntaxException if provided url is malformed
     */
    public static AuthorizationCodeResponse parse(String redirectUrl) throws URISyntaxException {
        Map<String, String> params = UrlEncodedUtils.parse(redirectUrl);
        return new AuthorizationCodeResponse(params.get("code"), Error.parse(params.get("error")),
                params.get("error_description"));
    }
}
