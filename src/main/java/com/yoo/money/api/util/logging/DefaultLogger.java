/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.util.logging;

import java.util.logging.Level;

/**
 * Default {@link Logger} implementation.
 */
public final class DefaultLogger implements Logger {

    private static final DefaultLogger INSTANCE = new DefaultLogger();

    private DefaultLogger() {
    }

    /**
     * @return an instance of {@link DefaultLogger}
     */
    public static DefaultLogger getInstance() {
        return INSTANCE;
    }

    @Override
    public void d(String tag, String msg) {
        log(Level.FINE, tag, msg);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        log(Level.FINE, tag, msg, tr);
    }

    @Override
    public void e(String tag, String msg) {
        log(Level.SEVERE, tag, msg);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        log(Level.SEVERE, tag, msg, tr);
    }

    @Override
    public void i(String tag, String msg) {
        log(Level.INFO, tag, msg);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        log(Level.INFO, tag, msg, tr);
    }

    @Override
    public void v(String tag, String msg) {
        log(Level.ALL, tag, msg);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        log(Level.ALL, tag, msg, tr);
    }

    @Override
    public void w(String tag, String msg) {
        log(Level.WARNING, tag, msg);
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        log(Level.WARNING, tag, msg, tr);
    }

    private static void log(Level level, String tag, String msg) {
        getLogger(tag).log(level, msg);
    }

    private static void log(Level level, String tag, String msg, Throwable tr) {
        getLogger(tag).log(level, msg, tr);
    }

    private static java.util.logging.Logger getLogger(String tag) {
        return java.util.logging.Logger.getLogger(tag);
    }
}
