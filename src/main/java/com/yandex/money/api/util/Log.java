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

package com.yandex.money.api.util;

import com.yandex.money.api.util.logging.DefaultLogger;

/**
 * Utility class for convenient logging
 *
 * @deprecated use {@link DefaultLogger} instead
 */
@Deprecated
public final class Log {

    private Log() {
    }

    /**
     * Logs message at log level {@code Level.ALL}
     *
     * @param msg message to log
     */
    public static void v(String msg) {
        com.yandex.money.api.util.logging.Log.v(msg);
    }

    /**
     * Logs message at log level {@code Level.INFO}
     *
     * @param msg message to log
     */
    public static void i(String msg) {
        com.yandex.money.api.util.logging.Log.i(msg);
    }

    /**
     * Logs message at log level {@code Level.WARNING}
     *
     * @param msg message to log
     */
    public static void w(String msg) {
        com.yandex.money.api.util.logging.Log.w(msg);
    }

    /**
     * Logs message at log level {@code Level.WARNING}
     *
     * @param msg message to log
     * @param e   Throwable associated with log message.
     */
    public static void w(String msg, Throwable e) {
        com.yandex.money.api.util.logging.Log.w(msg, e);
    }

    /**
     * Logs message at log level {@code Level.SEVERE}
     *
     * @param msg message to log
     */
    public static void e(String msg) {
        com.yandex.money.api.util.logging.Log.e(msg);
    }

    /**
     * Logs message at log level {@code Level.SEVERE}
     *
     * @param msg message to log
     * @param e   Throwable associated with log message.
     */
    public static void e(String msg, Throwable e) {
        com.yandex.money.api.util.logging.Log.e(msg, e);
    }
}
