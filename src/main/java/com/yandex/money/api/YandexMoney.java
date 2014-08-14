package com.yandex.money.api;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.net.ApiClient;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.OAuth2Session;

import java.io.IOException;

public class YandexMoney {

    private final OAuth2Session session;

    public YandexMoney(String clientId) {
        this(new DefaultApiClient(clientId));
    }

    public YandexMoney(ApiClient apiClient) {
        session = new OAuth2Session(apiClient);
    }

    public <T> T execute(MethodRequest<T> methodRequest) throws IOException,
            InsufficientScopeException, InvalidTokenException, InvalidRequestException {
        return session.execute(methodRequest);
    }

    public void setAccessToken(String accessToken) {
        session.setAccessToken(accessToken);
    }

    public void setDebugLogging(boolean debugLogging) {
        session.setDebugLogging(debugLogging);
    }
}
