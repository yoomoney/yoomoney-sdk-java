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

package com.yoo.money.api.methods.payment.params;

import java.util.Map;

import static com.yoo.money.api.util.Common.checkNotEmpty;
import static com.yoo.money.api.util.Common.checkNotNull;
import static java.util.Collections.unmodifiableMap;

/**
 * @author Anton Ermak (support@yoomoney.ru).
 */
public abstract class PaymentParams {

    public final String patternId;
    public final Map<String, String> paymentParams;

    PaymentParams(String patternId, Map<String, String> paymentParams) {
        this.patternId = checkNotEmpty(patternId, "patternId");
        this.paymentParams = unmodifiableMap(checkNotNull(paymentParams, "paymentParams"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentParams that = (PaymentParams) o;

        return patternId.equals(that.patternId) && paymentParams.equals(that.paymentParams);
    }

    @Override
    public int hashCode() {
        int result = patternId.hashCode();
        result = 31 * result + paymentParams.hashCode();
        return result;
    }
}
