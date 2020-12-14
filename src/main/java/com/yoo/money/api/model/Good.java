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

package com.yoo.money.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Describes digital item, that user can obtain when paying for them.
 */
public class Good {

    /**
     * serial number
     */
    @SerializedName("serial")
    public final String serial;

    /**
     * secret
     */
    @SerializedName("secret")
    public final String secret;

    /**
     * secret url
     */
    @SerializedName("secretUrl")
    public final String secretUrl;

    /**
     * merchant article id
     */
    @SerializedName("merchantArticleId")
    public final String merchantArticleId;

    /**
     * Constructor.
     *
     * @param serial serial number
     * @param secret secret
     * @param merchantArticleId merchant article id
     */
    public Good(String serial, String secret, String merchantArticleId) {
        this(serial, secret, null, merchantArticleId);
    }

    /**
     * Constructor.
     *
     * @param serial serial number
     * @param secret secret
     * @param secretUrl secretUrl
     * @param merchantArticleId merchant article id
     */
    public Good(String serial, String secret, String secretUrl, String merchantArticleId) {
        this.serial = serial;
        this.secret = secret;
        this.secretUrl = secretUrl;
        this.merchantArticleId = merchantArticleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Good good = (Good) o;

        return (serial == null ? good.serial == null : serial.equals(good.serial)) &&
                (secret == null ? good.secret == null : secret.equals(good.secret)) &&
                (secretUrl == null ? good.secretUrl == null : secretUrl.equals(good.secretUrl)) &&
                (merchantArticleId == null ? good.merchantArticleId == null :
                        merchantArticleId.equals(good.merchantArticleId));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (serial == null ? 0 : serial.hashCode());
        result = prime * result + (secret == null ? 0 : secret.hashCode());
        result = prime * result + (secretUrl == null ? 0 : secretUrl.hashCode());
        result = prime * result + (merchantArticleId == null ? 0 : merchantArticleId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Good{" +
                "serial='" + serial + '\'' +
                ", secret='" + secret + '\'' +
                ", secretUrl=" + secretUrl + '\'' +
                ", merchantArticleId='" + merchantArticleId + '\'' +
                '}';
    }
}
