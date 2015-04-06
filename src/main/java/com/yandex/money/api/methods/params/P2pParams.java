/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
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

package com.yandex.money.api.methods.params;

import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for P2P payment parameters.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class P2pParams implements Params {

    public static final String PATTERN_ID = "p2p";

    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT_DUE = "amount_due";
    private static final String PARAM_MESSAGE = "message";

    private final String to;
    private final BigDecimal amountDue;
    private final String message;

    /**
     * Constructor.
     *
     * @param to recipient's account number
     * @param amountDue amount to receive
     * @param message message to a recipient
     */
    public P2pParams(String to, BigDecimal amountDue, String message) {
        if (Strings.isNullOrEmpty(to))
            throw new IllegalArgumentException(PARAM_TO + " is null or empty");
        this.to = to;

        if (amountDue == null)
            throw new IllegalArgumentException(PARAM_AMOUNT_DUE + " is null or empty");
        this.amountDue = amountDue;

        this.message = message;
    }

    /**
     * Constructor.
     *
     * @param to recipient's account number
     * @param amountDue amount to receive
     */
    public P2pParams(String to, BigDecimal amountDue) {
        this(to, amountDue, null);
    }

    @Override
    public String getPatternId() {
        return PATTERN_ID;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_TO, to);

        result.put(PARAM_AMOUNT_DUE, amountDue.toPlainString());

        if (!Strings.isNullOrEmpty(message)) {
            result.put(PARAM_MESSAGE, message);
        }

        return result;
    }
}
