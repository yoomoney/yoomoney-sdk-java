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

package com.yandex.money.api.processes;

import com.yandex.money.api.methods.payment.BaseProcessPayment;
import com.yandex.money.api.methods.payment.BaseRequestPayment;
import com.yandex.money.api.model.Identifiable;

import java.util.Map;

/**
 * Interface for all payment processes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface IPaymentProcess extends Process {

    /**
     * Resets payment process to its initial state.
     */
    void reset();

    /**
     * @return base request payment object
     */
    BaseRequestPayment getRequestPayment();

    /**
     * @return base process payment object
     */
    BaseProcessPayment getProcessPayment();

    /**
     * Provides parameters to perform payment process.
     */
    interface ParameterProvider {

        /**
         * @return pattern id
         */
        String getPatternId();

        /**
         * @return key-value pairs of payment parameters
         */
        Map<String, String> getPaymentParameters();

        /**
         * @return selected money source
         */
        Identifiable getMoneySource();

        /**
         * @return CSC code if required
         */
        String getCsc();

        /**
         * @return success URI
         */
        String getExtAuthSuccessUri();

        /**
         * @return fail URI
         */
        String getExtAuthFailUri();
    }
}
