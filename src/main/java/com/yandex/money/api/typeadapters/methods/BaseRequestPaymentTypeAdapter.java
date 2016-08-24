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

package com.yandex.money.api.typeadapters.methods;

import com.google.gson.JsonObject;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.methods.BaseRequestPayment.Status;
import com.yandex.money.api.model.Error;

import static com.yandex.money.api.typeadapters.JsonUtils.getBigDecimal;
import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryString;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
final class BaseRequestPaymentTypeAdapter {

    private static final String MEMBER_CONTRACT_AMOUNT = "contract_amount";
    private static final String MEMBER_ERROR = "error";
    private static final String MEMBER_REQUEST_ID = "request_id";
    private static final String MEMBER_STATUS = "status";
    private static final String MEMBER_TITLE = "title";

    private BaseRequestPaymentTypeAdapter() {
    }

    static final class Delegate {

        private Delegate() {
        }

        static <T extends BaseRequestPayment.Builder> void deserialize(JsonObject object, T builder) {
            checkNotNull(builder, "builder")
                    .setStatus(Status.parse(getMandatoryString(checkNotNull(object, "object"), MEMBER_STATUS)))
                    .setError(Error.parse(getString(object, MEMBER_ERROR)))
                    .setContractAmount(getBigDecimal(object, MEMBER_CONTRACT_AMOUNT))
                    .setRequestId(getString(object, MEMBER_REQUEST_ID))
                    .setTitle(getString(object, MEMBER_TITLE));
        }

        static <T extends BaseRequestPayment> void serialize(JsonObject object, T value) {
            checkNotNull(object, "object")
                    .addProperty(MEMBER_STATUS, checkNotNull(value, "value").status.code);
            if (value.error != null) {
                object.addProperty(MEMBER_ERROR, value.error.code);
            } else {
                object.addProperty(MEMBER_REQUEST_ID, value.requestId);
                object.addProperty(MEMBER_CONTRACT_AMOUNT, value.contractAmount);
                object.addProperty(MEMBER_TITLE, value.title);
            }
        }
    }
}
