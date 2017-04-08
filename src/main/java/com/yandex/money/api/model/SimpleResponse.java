/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NBCO Yandex.Money LLC
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

import com.google.gson.annotations.SerializedName;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Extended info about status of a response. Holds error, if request wasn't successful.
 */
public class SimpleResponse {

    /**
     * Status of the response.
     */
    @SerializedName("status")
    public final SimpleStatus status;
    /**
     * Error info, if response was unsuccessful.
     */
    @SerializedName("error")
    public final Error error;

    public SimpleResponse(SimpleStatus status, Error error) {
        this.status = checkNotNull(status, "status");
        if (status != SimpleStatus.SUCCESS) {
            checkNotNull(error, "error");
        }
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleResponse that = (SimpleResponse) o;

        return status == that.status && error == that.error;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatusInfo{" +
                "status=" + status +
                ", error=" + error +
                '}';
    }

    public final boolean isSuccessful() {
        return status == SimpleStatus.SUCCESS;
    }
}
