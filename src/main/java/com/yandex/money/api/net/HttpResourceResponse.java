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

package com.yandex.money.api.net;

import org.joda.time.DateTime;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * This class wraps HTTP resource obtained from remove server.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public final class HttpResourceResponse<T> {

    /**
     * State.
     */
    public final ResourceState resourceState;

    /**
     * Content-type header. May be {@code null}.
     */
    public final String contentType;

    /**
     * Last modified header.
     */
    public final DateTime lastModified;

    /**
     * Expires header.
     */
    public final DateTime expires;

    /**
     * Document. May be {@code null}.
     */
    public final T document;

    HttpResourceResponse(ResourceState resourceState, String contentType, DateTime lastModified,
                         DateTime expires, T document) {

        this.resourceState = checkNotNull(resourceState, "resourceState");
        this.lastModified = checkNotNull(lastModified, "lastModified");
        this.contentType = contentType;
        this.expires = expires;
        this.document = document;
    }

    public boolean hasDocument() {
        return resourceState == HttpResourceResponse.ResourceState.DOCUMENT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpResourceResponse<?> that = (HttpResourceResponse<?>) o;

        return resourceState == that.resourceState &&
                !(contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) &&
                lastModified.equals(that.lastModified) &&
                !(expires != null ? !expires.equals(that.expires) : that.expires != null) &&
                !(document != null ? !document.equals(that.document) : that.document != null);
    }

    @Override
    public int hashCode() {
        int result = resourceState.hashCode();
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + lastModified.hashCode();
        result = 31 * result + (expires != null ? expires.hashCode() : 0);
        result = 31 * result + (document != null ? document.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpResourceResponse{" +
                "resourceState=" + resourceState +
                ", contentType='" + contentType + '\'' +
                ", lastModified=" + lastModified +
                ", expires=" + expires +
                ", document=\n" + document +
                "\n}";
    }

    public enum ResourceState {

        /**
         * Resource has been downloaded successfully and can be processed.
         */
        DOCUMENT,

        /**
         * Resource hasn't been changed. Instance doesn't contain document and cached copy should be
         * used for further processing.
         */
        NOT_MODIFIED
    }
}
