package com.yandex.money.exceptions;

/**
 * API и OAuth2.
 * Обязательные параметры запроса отсутствуют или имеют некорректные или недопустимые значения.
 * Формат запроса не соответствует протоколу.
 * Запрос невозможно разобрать.
 * Приложению следует изменить параметры/формат запроса и отправить запрос повторно.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class InvalidRequestException extends Exception {

    public InvalidRequestException(String error) {
        super(error);
    }
}
