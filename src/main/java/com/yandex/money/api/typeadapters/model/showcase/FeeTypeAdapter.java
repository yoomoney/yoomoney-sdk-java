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

package com.yandex.money.api.typeadapters.model.showcase;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.CustomFee;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.StdFee;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Convenient class to serialize/deserialize various {@link Fee} implementations.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class FeeTypeAdapter extends BaseTypeAdapter<Fee> {

    private static final String MEMBER_TYPE = "type";

    private static final FeeTypeAdapter INSTANCE = new FeeTypeAdapter();

    private static final String TYPE_CUSTOM = "custom";
    private static final String TYPE_STD = "std";

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
    public Fee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String type = Delegate.getFeeType(json.getAsJsonObject());
        switch (type) {
            case TYPE_CUSTOM:
                return CustomFeeTypeAdapter.getInstance().fromJson(json);
            case TYPE_STD:
                return StdFeeTypeAdapter.getInstance().fromJson(json);
            default:
                throw new JsonParseException("unknown fee type = " + type);
        }
    }

    @Override
    public JsonElement serialize(Fee src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }

    @Override
    public Class<Fee> getType() {
        return Fee.class;
    }

    static final class Delegate {

        private static final Map<Class<? extends Fee>, String> FEE_TYPE_MAPPING;

        static {
            Map<Class<? extends Fee>, String> feeTypeMapping = new HashMap<>();
            feeTypeMapping.put(CustomFee.class, TYPE_CUSTOM);
            feeTypeMapping.put(StdFee.class, TYPE_STD);
            FEE_TYPE_MAPPING = Collections.unmodifiableMap(feeTypeMapping);
        }

        private Delegate() {
        }

        static void checkFeeType(JsonObject fee, Class<? extends Fee> clazz) {
            String actual = getFeeType(fee);
            String expected = FEE_TYPE_MAPPING.get(clazz);
            if (actual == null || !actual.equals(expected)) {
                throw new IllegalStateException("fee type should be \"" + expected + "\" but \""
                        + actual + "\" given");
            }
        }

        public static void serialize(JsonObject to, Class<? extends Fee> clazz) {
            to.addProperty(FeeTypeAdapter.MEMBER_TYPE, FEE_TYPE_MAPPING.get(clazz));
        }

        static String getFeeType(JsonObject jsonObject) {
            return getString(jsonObject, MEMBER_TYPE);
        }
    }
}
