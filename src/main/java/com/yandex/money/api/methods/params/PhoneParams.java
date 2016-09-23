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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Convenience class for phone top up parameters.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public final class PhoneParams extends PaymentParams {

    public static final String PATTERN_ID = "phone-topup";

    public static PhoneParams newInstance(String number, BigDecimal amount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone-number", checkNotEmpty(number, "number"));
        params.put("amount", checkNotNull(amount, "amount").toPlainString());
        return new PhoneParams(PATTERN_ID, params);
    }

    private PhoneParams(String patternId, Map<String, String> paymentParams) {
        super(patternId, paymentParams);
    }
}
