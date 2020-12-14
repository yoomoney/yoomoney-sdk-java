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
import com.yoo.money.api.time.DateTime;

import static com.yoo.money.api.util.Common.checkNotEmpty;
import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Describes avatar from {@link com.yoo.money.api.methods.wallet.AccountInfo}.
 */
public final class Avatar {

    /**
     * url to avatar
     */
    @SerializedName("url")
    public final String url;

    /**
     * avatar change time
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("ts")
    public final DateTime timestamp;

    /**
     * Constructor.
     *
     * @param url url to avatar
     * @param timestamp avatar change time
     */
    public Avatar(String url, DateTime timestamp) {
        this.url = checkNotEmpty(url, "url");
        this.timestamp = checkNotNull(timestamp, "timestamp");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Avatar avatar = (Avatar) o;

        //noinspection SimplifiableIfStatement
        if (url != null ? !url.equals(avatar.url) : avatar.url != null) return false;
        return timestamp != null ? timestamp.equals(avatar.timestamp) : avatar.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "url='" + url + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
