/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.util.logging;

/**
 * Implementation of this interface allows log messages for a specific system or application component.
 */
public interface Logger {

    /**
     * Send a debug log message.
     *
     * @param tag tag
     * @param msg message
     */
    void d(String tag, String msg);

    /**
     * Send a debug log message and log the exception.
     *
     * @param tag tag
     * @param msg message
     * @param tr exception
     */
    void d(String tag, String msg, Throwable tr);

    /**
     * Send an error log message.
     *
     * @param tag tag
     * @param msg message
     */
    void e(String tag, String msg);

    /**
     * Send an error log message and log the exception.
     *
     * @param tag tag
     * @param msg message
     * @param tr exception
     */
    void e(String tag, String msg, Throwable tr);

    /**
     * Send an info log message.
     *
     * @param tag tag
     * @param msg message
     */
    void i(String tag, String msg);

    /**
     * Send an info log message and log the exception.
     *
     * @param tag tag
     * @param msg message
     * @param tr exception
     */
    void i(String tag, String msg, Throwable tr);

    /**
     * Send a verbose log message.
     *
     * @param tag tag
     * @param msg message
     */
    void v(String tag, String msg);

    /**
     * Send a verbose log message and log the exception.
     *
     * @param tag tag
     * @param msg message
     * @param tr exception
     */
    void v(String tag, String msg, Throwable tr);

    /**
     * Send a warning log message.
     *
     * @param tag tag
     * @param msg message
     */
    void w(String tag, String msg);

    /**
     * Send a warning log message and log the exception.
     *
     * @param tag tag
     * @param msg message
     * @param tr exception
     */
    void w(String tag, String msg, Throwable tr);
}
