package com.yandex.money.api.exceptions;

/**
 * Thrown when {@link com.yandex.money.api.net.OAuth2Session} is authorized with an access token
 * that has no required permissions ({@link com.yandex.money.api.net.Scope}) to perform an operation
 * (call API method).
 * <p/>
 * If your application requires to perform this operation you should receive a new token with
 * sufficient scopes.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 * @see com.yandex.money.api.net.Scope
 * @see com.yandex.money.api.net.OAuth2Session
 */
public final class InsufficientScopeException extends Exception {
    public InsufficientScopeException(String error) {
        super(error);
    }
}
