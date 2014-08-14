package com.yandex.money.api.net;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;

import java.io.IOException;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
interface Session {
    <T> T execute(MethodRequest<T> request) throws IOException, InvalidRequestException,
            InvalidTokenException, InsufficientScopeException;
}
