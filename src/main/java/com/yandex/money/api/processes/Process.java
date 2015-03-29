package com.yandex.money.api.processes;

import com.squareup.okhttp.Call;
import com.yandex.money.api.net.OAuth2Session;

/**
 * Provides interface for every process.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
interface Process {

    /**
     * Tries to perform next step of a process.
     *
     * @return {@code true} if process is completed
     * @throws Exception if something went wrong
     */
    boolean proceed() throws Exception;

    /**
     * Tries to perform next step of a process asynchronously.
     *
     * @param callback callback
     * @param <T> type of response
     * @return a {@link Call} object that can be canceled
     */
    <T> Call proceed(OAuth2Session.OnResponseReady<T> callback) throws Exception;

    /**
     * Tries to repeat the step of a process.
     *
     * @return {@code true} if process is completed
     * @throws Exception if something went wrong
     */
    boolean repeat() throws Exception;

    /**
     * Tries to perform next step of a process asynchronously.
     *
     * @param callback callback
     * @param <T> type of response
     * @return a {@link Call} object that can be canceled
     */
    <T> Call repeat(OAuth2Session.OnResponseReady<T> callback) throws Exception;
}
