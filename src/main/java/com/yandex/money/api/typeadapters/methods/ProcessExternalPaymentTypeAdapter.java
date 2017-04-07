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
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link ProcessExternalPayment}
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ProcessExternalPaymentTypeAdapter extends BaseTypeAdapter<ProcessExternalPayment> {

    private static final ProcessExternalPaymentTypeAdapter INSTANCE = new ProcessExternalPaymentTypeAdapter();

    private static final String MEMBER_MONEY_SOURCE = "money_source";

    private ProcessExternalPaymentTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static ProcessExternalPaymentTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public ProcessExternalPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        ExternalCard moneySource = context.deserialize(jsonObject.get(MEMBER_MONEY_SOURCE), ExternalCard.class);
        ProcessExternalPayment.Builder builder = new ProcessExternalPayment.Builder()
                .setExternalCard(moneySource);
        BaseProcessPaymentTypeAdapter.Delegate.deserialize(jsonObject, builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(ProcessExternalPayment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(MEMBER_MONEY_SOURCE, context.serialize(src.externalCard));
        BaseProcessPaymentTypeAdapter.Delegate.serialize(jsonObject, src);
        return jsonObject;
    }

    @Override
    public Class<ProcessExternalPayment> getType() {
        return ProcessExternalPayment.class;
    }
}
