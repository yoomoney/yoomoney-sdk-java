package com.yandex.money.api.processes;

import com.squareup.okhttp.Call;

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
     * @return a {@link Call} object that can be canceled
     */
    Call proceedAsync() throws Exception;

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
     * @return a {@link Call} object that can be canceled
     */
    Call repeatAsync() throws Exception;
}
