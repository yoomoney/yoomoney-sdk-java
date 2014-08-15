package com.yandex.money.api;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.net.ApiClient;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.OAuth2Session;

import java.io.IOException;

/**
 * This is a starting point for all applications. Using this class you can execute any API method as
 * well as perform other operations.
 * <p/>
 * For example:
 * <pre>
 * {@code   YandexMoney ym = new YandexMoney("client_id");
 *     ym.setAccessToken("access_token");
 *     AccountInfo accountInfo = ym.execute(new AccountInfo.Request());
 * }
 * </pre>
 */
public class YandexMoney {

    private final OAuth2Session session;

    /**
     * Creates YandexMoney instance using {@link com.yandex.money.api.net.DefaultApiClient}
     * implementation and specified client id.
     *
     * @param clientId client id
     */
    public YandexMoney(String clientId) {
        this(new DefaultApiClient(clientId));
    }

    /**
     * Creates YandexMoney instance using custom {@link com.yandex.money.api.net.ApiClient}.
     *
     * @param apiClient API client
     */
    public YandexMoney(ApiClient apiClient) {
        session = new OAuth2Session(apiClient);
    }

    /**
     * @see com.yandex.money.api.net.OAuth2Session#execute(com.yandex.money.api.net.MethodRequest)
     */
    public <T> T execute(MethodRequest<T> methodRequest) throws IOException,
            InsufficientScopeException, InvalidTokenException, InvalidRequestException {
        return session.execute(methodRequest);
    }

    /**
     * @see com.yandex.money.api.net.OAuth2Session#setAccessToken(String)
     */
    public void setAccessToken(String accessToken) {
        session.setAccessToken(accessToken);
    }

    /**
     * @see com.yandex.money.api.net.OAuth2Session#setDebugLogging(boolean)
     */
    public void setDebugLogging(boolean debugLogging) {
        session.setDebugLogging(debugLogging);
    }
}
