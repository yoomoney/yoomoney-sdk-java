package com.yandex.money.net;

import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;

import java.io.IOException;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
interface Session {
    <T> T execute(MethodRequest<T> request) throws IOException, InvalidRequestException,
            InvalidTokenException, InsufficientScopeException;
}
