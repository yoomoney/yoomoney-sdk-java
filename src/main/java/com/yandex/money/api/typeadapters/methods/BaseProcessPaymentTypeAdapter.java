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
import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.model.Error;

import static com.yandex.money.api.typeadapters.JsonUtils.getLong;
import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryString;
import static com.yandex.money.api.typeadapters.JsonUtils.getNotNullMap;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.typeadapters.JsonUtils.toJsonObject;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
final class BaseProcessPaymentTypeAdapter {

    private static final String MEMBER_ACS_PARAMS = "acs_params";
    private static final String MEMBER_ACS_URI = "acs_uri";
    private static final String MEMBER_ERROR = "error";
    private static final String MEMBER_NEXT_RETRY = "next_retry";
    private static final String MEMBER_STATUS = "status";

    private BaseProcessPaymentTypeAdapter() {
    }

    static final class Delegate {

        private static final String MEMBER_INVOICE_ID = "invoice_id";

        private Delegate() {
        }

        static <T extends BaseProcessPayment.Builder> void deserialize(JsonObject object, T builder) {
            checkNotNull(builder, "builder")
                    .setStatus(BaseProcessPayment.Status.parse(
                            getMandatoryString(checkNotNull(object, "object"), MEMBER_STATUS)))
                    .setError(Error.parse(getString(object, MEMBER_ERROR)))
                    .setInvoiceId(getString(object, MEMBER_INVOICE_ID))
                    .setAcsUri(getString(object, MEMBER_ACS_URI))
                    .setAcsParams(getNotNullMap(object, MEMBER_ACS_PARAMS));

            Long nextRetry = getLong(object, MEMBER_NEXT_RETRY);
            if (nextRetry != null) {
                builder.setNextRetry(nextRetry);
            }
        }


        static <T extends BaseProcessPayment> void serialize(JsonObject object, T value) {
            checkNotNull(object, "object")
                    .addProperty(MEMBER_STATUS, checkNotNull(value, "value").status.code);
            if (value.error != null) {
                object.addProperty(MEMBER_ERROR, value.error.code);
            } else {
                object.addProperty(MEMBER_ACS_URI, value.acsUri);
                object.addProperty(MEMBER_INVOICE_ID, value.invoiceId);
                object.add(MEMBER_ACS_PARAMS, toJsonObject(value.acsParams));
                object.addProperty(MEMBER_NEXT_RETRY, value.nextRetry);
            }
        }
    }
}
