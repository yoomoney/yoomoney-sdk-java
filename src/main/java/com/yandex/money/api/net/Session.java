package com.yandex.money.api.net;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;

import java.io.IOException;

/**
 * Session interface. Allows to execute API requests.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
interface Session {
    /**
     * Executes API request in authorized or non-authorized session.
     *
     * @param request request
     * @param <T> respond type
     * @return respond
     */
    <T> T execute(MethodRequest<T> request) throws IOException, InvalidRequestException,
            InvalidTokenException, InsufficientScopeException;
}
