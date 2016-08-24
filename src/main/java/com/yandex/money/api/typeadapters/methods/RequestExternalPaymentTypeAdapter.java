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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link RequestExternalPayment}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class RequestExternalPaymentTypeAdapter extends BaseTypeAdapter<RequestExternalPayment> {

    private static final RequestExternalPaymentTypeAdapter INSTANCE = new RequestExternalPaymentTypeAdapter();

    private RequestExternalPaymentTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static RequestExternalPaymentTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public RequestExternalPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        RequestExternalPayment.Builder builder = new RequestExternalPayment.Builder();
        BaseRequestPaymentTypeAdapter.Delegate.deserialize(jsonObject, builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(RequestExternalPayment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        BaseRequestPaymentTypeAdapter.Delegate.serialize(jsonObject, src);
        return jsonObject;
    }

    @Override
    protected Class<RequestExternalPayment> getType() {
        return RequestExternalPayment.class;
    }
}
