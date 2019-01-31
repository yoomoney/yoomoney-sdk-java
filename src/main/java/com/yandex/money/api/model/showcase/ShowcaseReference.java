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

import com.google.gson.annotations.SerializedName;
import com.yandex.money.api.methods.ShowcaseSearch;
import com.yandex.money.api.util.Enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static com.yandex.money.api.util.Common.checkNotEmpty;

/**
 * Element of {@link ShowcaseSearch} class.
 */
public final class ShowcaseReference {

    /**
     * Showcase ID.
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("id")
    public final long scid;

    /**
     * Title.
     */
    @SerializedName("title")
    public final String title;

    /**
     * Index of an item in list (lower is higher ranking). May be {@code null}
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("top")
    public final Integer topIndex;

    /**
     * URL to submit params of the first step if applicable. May be {@code null}.
     */
    @SerializedName("url")
    public final String url;

    /**
     * Showcase parameters.
     */
    @SerializedName("params")
    public final Map<String, String> params;

    /**
     * Showcase format. JSON or unknown one.
     */
    @SerializedName("format")
    public final Format format;

    /**
     * Describing possibility to use bonuses in showcase
     */
    @SerializedName("bonus_points")
    public final BonusUseType[] bonusPoints;

    ShowcaseReference(Builder builder) {
        scid = builder.scid;
        title = checkNotEmpty(builder.title, "title");
        topIndex = builder.topIndex;
        url = builder.url;
        format = builder.format;
        params = builder.params != null ? Collections.unmodifiableMap(builder.params) : null;
        bonusPoints = builder.bonusPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowcaseReference that = (ShowcaseReference) o;

        if (scid != that.scid) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (topIndex != null ? !topIndex.equals(that.topIndex) : that.topIndex != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        //noinspection SimplifiableIfStatement
        if (params != null ? !params.equals(that.params) : that.params != null) return false;
        if (bonusPoints != null ? !Arrays.equals(bonusPoints, that.bonusPoints)
                : that.bonusPoints != null) return false;
        return format == that.format;
    }

    @Override
    public int hashCode() {
        int result = (int) (scid ^ (scid >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (topIndex != null ? topIndex.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (bonusPoints != null ? Arrays.hashCode(bonusPoints) : 0);
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
                ", bonusPoints=" + bonusPoints +
                '}';
    }

    /**
     * Format.
     */
    public enum Format implements Enums.WithCode<Format> {
        /**
         * JSON.
         */
        @SerializedName("json")
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
    }

    public enum BonusUseType {
        @SerializedName("spending")
        SPENDING,
        @SerializedName("earning")
        EARNING
    }

    public final static class Builder {

        long scid;
        String title;
        Integer topIndex;
        Format format;
        String url = null;
        Map<String, String> params;
        BonusUseType[] bonusPoints;

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
            this.params = params;
            return this;
        }

        public Builder setBonusPoints(BonusUseType[] bonusPoints) {
            this.bonusPoints = bonusPoints;
            return this;
        }

        public ShowcaseReference create() {
            return new ShowcaseReference(this);
        }
    }
}
