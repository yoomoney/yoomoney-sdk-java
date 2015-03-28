package com.yandex.money.api.processes;

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
     * Tries to repeat the step of a process.
     *
     * @return {@code true} if process is completed
     * @throws Exception if something went wrong
     */
    boolean repeat() throws Exception;
}
