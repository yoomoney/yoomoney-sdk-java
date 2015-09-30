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

package com.yandex.money.api.typeadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.Fee;

import java.lang.reflect.Type;

/**
 * Convenient class to serialize/deserialize various {@link Fee} implementations.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class FeeTypeAdapter extends BaseTypeAdapter<Fee> {

    public static final String MEMBER_TYPE = "type";
    private static final FeeTypeAdapter INSTANCE = new FeeTypeAdapter();

    private FeeTypeAdapter() {
        StdFeeTypeAdapter.getInstance();
        CustomFeeTypeAdapter.getInstance();
    }

    /**
     * @return instance of this class
     */
    public static FeeTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public Fee deserialize(JsonElement json, Type typeOfT,
                           JsonDeserializationContext context) throws JsonParseException {
        String type = Helper.getFeeType(json.getAsJsonObject());

        if (CustomFeeTypeAdapter.TYPE_CUSTOM.equals(type)) {
            return CustomFeeTypeAdapter.getInstance().fromJson(json);
        } else if (StdFeeTypeAdapter.TYPE_STD.equals(type)) {
            return StdFeeTypeAdapter.getInstance().fromJson(json);
        } else {
            throw new JsonParseException("unknown fee type = " + type);
        }
    }

    @Override
    public JsonElement serialize(Fee src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }

    @Override
    protected Class<Fee> getType() {
        return Fee.class;
    }

    static final class Helper {

        private Helper() {
        }

        public static void checkFeeType(JsonObject fee, String expected) {
            String type = getFeeType(fee);
            if (!type.equals(expected)) {
                throw new IllegalStateException("fee type should be \"" + expected + "\" but \""
                        + type + "\" given");
            }
        }

        private static String getFeeType(JsonObject jsonObject) {
            String type = JsonUtils.getString(jsonObject, MEMBER_TYPE);
            if (type == null) {
                throw new NullPointerException("fee type is null");
            }
            return type;
        }
    }
}
