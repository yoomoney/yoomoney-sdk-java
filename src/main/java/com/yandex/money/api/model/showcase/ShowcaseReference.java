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

package com.yandex.money.api.model.showcase;

import com.yandex.money.api.methods.ShowcaseSearch;
import com.yandex.money.api.utils.Enums;

import java.util.Collections;
import java.util.Map;

import static com.yandex.money.api.utils.Common.checkNotEmpty;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Element of {@link ShowcaseSearch} class.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseReference {

    /**
     * Showcase ID.
     */
    public final long scid;

    /**
     * Title.
     */
    public final String title;

    /**
     * Index of an item in list (lower is higher ranking). May be {@code null}
     */
    public final Integer topIndex;

    /**
     * URL to submit params of the first step if applicable. May be {@code null}.
     */
    public final String url;

    /**
     * Showcase parameters.
     */
    public final Map<String, String> params;

    /**
     * Showcase format. JSON or unknown one.
     */
    public final Format format;

    /**
     * Constructor.
     *
     * @param scid showcase ID
     * @param title    title of an item
     * @param topIndex index of an item in list (lower values mean higher ranking, if values are
     *                 equal it is recommended to sort {@link ShowcaseReference}'s by
     *                 title), can be null
     * @param format   showcase format
     * @deprecated     use {@link com.yandex.money.api.model.showcase.ShowcaseReference.Builder} instead
     */
    @Deprecated
    public ShowcaseReference(long scid, String title, Integer topIndex, Format format) {
        this(new Builder().setScid(scid)
                .setTitle(title)
                .setTopIndex(topIndex)
                .setFormat(format));
    }

    /**
     * Constructor.
     *
     * @param scid showcase ID
     * @param title    title of an item
     * @param topIndex index of an item in list (lower values mean higher ranking, if values are
     *                 equal it is recommended to sort {@link ShowcaseReference}'s by
     *                 title), can be null
     * @param url      url to submit {@code params} of the first step if applicable, can be null
     * @param format   showcase format
     * @param params   showcase parameters of the first step, can be null
     * @deprecated     use {@link com.yandex.money.api.model.showcase.ShowcaseReference.Builder} instead
     */
    @Deprecated
    public ShowcaseReference(long scid, String title, Integer topIndex, String url, Format format,
                             Map<String, String> params) {

        this(new Builder().setScid(scid)
                .setTitle(title)
                .setTopIndex(topIndex)
                .setUrl(url)
                .setFormat(format)
                .setParams(params));
    }

    private ShowcaseReference(Builder builder) {
        scid = builder.scid;
        title = checkNotEmpty(builder.title, "title");
        topIndex = builder.topIndex;
        url = builder.url;
        format = builder.format;
        params = Collections.unmodifiableMap(builder.params);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowcaseReference that = (ShowcaseReference) o;

        return scid == that.scid && title.equals(that.title)
                && !(topIndex != null ? !topIndex.equals(that.topIndex) : that.topIndex != null)
                && !(url != null ? !url.equals(that.url) : that.url != null)
                && params.equals(that.params) && format == that.format;
    }

    @Override
    public int hashCode() {
        int result = (int) (scid ^ (scid >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + (topIndex != null ? topIndex.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + params.hashCode();
        result = 31 * result + format.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ShowcaseReference{" +
                "scid=" + scid +
                ", title='" + title + '\'' +
                ", topIndex=" + topIndex +
                ", url='" + url + '\'' +
                ", params=" + params +
                ", format=" + format +
                '}';
    }

    /**
     * Format.
     */
    public enum Format implements Enums.WithCode<Format> {

        /**
         * JSON.
         */
        JSON("json");

        public final String code;

        Format(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Format[] getValues() {
            return values();
        }

        public static Format parse(String code) {
            return Enums.parse(JSON, code);
        }
    }

    public final static class Builder {

        private long scid;
        private String title;
        private Integer topIndex;
        private Format format;
        private String url = null;
        private Map<String, String> params = Collections.emptyMap();

        public Builder setScid(long scid) {
            this.scid = scid;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTopIndex(Integer topIndex) {
            this.topIndex = topIndex;
            return this;
        }

        public Builder setFormat(Format format) {
            this.format = format;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setParams(Map<String, String> params) {
            this.params = checkNotNull(params, "params");
            return this;
        }

        public ShowcaseReference create() {
            return new ShowcaseReference(this);
        }
    }
}
