package com.yandex.money.api.exceptions;

/**
 * Token is invalid.
 * <p/>
 * Possible causes:
 * <ul>
 *     <li>token does not exist;</li>
 *     <li>token is overdue;</li>
 *     <li>token has been revoked.</li>
 * </ul>
 * <p/>
 * The app should ask for a new token.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 * @see com.yandex.money.api.net.OAuth2Authorization
 * @see com.yandex.money.api.net.OAuth2Session
 */
public final class InvalidTokenException extends Exception {
    public InvalidTokenException(String error) {
        super(error);
    }
}
