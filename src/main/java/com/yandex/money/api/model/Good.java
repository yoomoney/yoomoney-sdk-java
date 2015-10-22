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

package com.yandex.money.api.model;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Describes digital item, that user can obtain when paying for them.
 */
public class Good {

    /**
     * serial number
     */
    public final String serial;

    /**
     * secret
     */
    public final String secret;

    /**
     * merchant article id
     */
    public final String merchantArticleId;

    /**
     * Constructor.
     *
     * @param serial serial number
     * @param secret secret
     * @param merchantArticleId merchant article id
     */
    public Good(String serial, String secret, String merchantArticleId) {
        checkNotNull(serial, "serial");
        checkNotNull(secret, "secret");

        this.serial = serial;
        this.secret = secret;
        this.merchantArticleId = merchantArticleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Good good = (Good) o;

        return serial.equals(good.serial) &&
                secret.equals(good.secret) &&
                !(merchantArticleId != null ? !merchantArticleId.equals(good.merchantArticleId) :
                        good.merchantArticleId != null);
    }

    @Override
    public int hashCode() {
        int result = serial.hashCode();
        result = 31 * result + secret.hashCode();
        result = 31 * result + (merchantArticleId != null ? merchantArticleId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Good{" +
                "serial='" + serial + '\'' +
                ", secret='" + secret + '\'' +
                ", merchantArticleId='" + merchantArticleId + '\'' +
                '}';
    }
}
