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

package com.yandex.money.api.typeadapters.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link AllowedMoneySource}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class AllowedMoneySourceTypeAdapter extends BaseTypeAdapter<AllowedMoneySource> {

    private static final AllowedMoneySourceTypeAdapter INSTANCE = new AllowedMoneySourceTypeAdapter();

    private AllowedMoneySourceTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static AllowedMoneySourceTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public AllowedMoneySource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return AllowedMoneySource.parseOrThrow(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(AllowedMoneySource src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.code);
    }

    @Override
    protected Class<AllowedMoneySource> getType() {
        return AllowedMoneySource.class;
    }
}
