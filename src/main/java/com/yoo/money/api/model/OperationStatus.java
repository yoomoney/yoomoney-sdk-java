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
import com.yoo.money.api.util.Constants;
import com.yoo.money.api.util.Enums;

/**
 * Status of any continuous operation that can be described by three states: success, refused and in progress.
 */
public enum OperationStatus implements Enums.WithCode<OperationStatus> {

    /**
     * Operation succeeded.
     */
    @SerializedName(Constants.Status.SUCCESS)
    SUCCESS(Constants.Status.SUCCESS),

    /**
     * Operation refused.
     */
    @SerializedName(Constants.Status.REFUSED)
    REFUSED(Constants.Status.REFUSED),

    /**
     * Operation is in progress.
     */
    @SerializedName(Constants.Status.IN_PROGRESS)
    IN_PROGRESS(Constants.Status.IN_PROGRESS);

    public final String code;

    OperationStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public OperationStatus[] getValues() {
        return values();
    }
}
