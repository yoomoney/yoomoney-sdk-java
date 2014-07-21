package com.yandex.money.exceptions;

/**
 * API.
 * Запрошена операция, на которую у токена нет прав.
 * Приложению следует отправить пользователя на OAuth2 процесс для получения нового токена с новыми правами.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class InsufficientScopeException extends Exception {
    public InsufficientScopeException(String error) {
        super(error);
    }
}
