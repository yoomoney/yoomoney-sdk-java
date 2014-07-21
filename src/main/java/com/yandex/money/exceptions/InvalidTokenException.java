package com.yandex.money.exceptions;

/**
 * API.
 * Ошибка для следующих случаев:
 * 1. несуществующий токен
 * 2. просроченный токен
 * 3. отозванный токен
 * <p/>
 * Приложению следует отправить пользователя на OAuth2 процесс для получения нового токена.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class InvalidTokenException extends Exception {
    public InvalidTokenException(String error) {
        super(error);
    }
}
