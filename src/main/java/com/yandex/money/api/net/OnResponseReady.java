package com.yandex.money.api.net;

/**
 * Callback for asynchronous execution of requests. Called when response is ready or when any
 * error occurred.
 *
 * @param <T> response type
 */
public interface OnResponseReady<T> {
    /**
     * Called when the request could not be executed due to cancellation, a connectivity problem
     * or timeout.
     *
     * @param exception the exception
     */
    void onFailure(Exception exception);

    /**
     * Called when the HTTP response was successfully returned by the remote server.
     *
     * @param response response
     */
    void onResponse(T response);
}
