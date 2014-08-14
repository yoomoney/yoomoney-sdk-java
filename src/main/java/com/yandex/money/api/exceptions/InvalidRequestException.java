package com.yandex.money.api.exceptions;

/**
 * Request is invalid.
 * <p/>
 * This exception is thrown when:
 * <ul>
 *     <li>mandatory method parameters are missing or have invalid or inconsistent values;</li>
 *     <li>request format does not meet the protocol;</li>
 *     <li>request can not be parsed;</li>
 * </ul>
 * You should change request parameters and send it again.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class InvalidRequestException extends Exception {
    public InvalidRequestException(String error) {
        super(error);
    }
}
