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

import com.google.gson.JsonParseException;
import com.yandex.money.api.utils.Strings;

import org.joda.time.DateTime;

/**
 * Describes avatar from {@link com.yandex.money.api.methods.AccountInfo}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Avatar {

    /**
     * url to avatar
     */
    public final String url;

    /**
     * avatar change time
     */
    public final DateTime timestamp;

    /**
     * Constructor.
     *
     * @param url url to avatar
     * @param timestamp avatar change time
     */
    public Avatar(String url, DateTime timestamp) {
        if (Strings.isNullOrEmpty(url)) {
            throw new JsonParseException("avatar url is null or empty");
        }
        if (timestamp == null) {
            throw new JsonParseException("avatar timestamp is null");
        }
        this.url = url;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "url='" + url + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Avatar avatar = (Avatar) o;

        return url.equals(avatar.url) && timestamp.equals(avatar.timestamp);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
