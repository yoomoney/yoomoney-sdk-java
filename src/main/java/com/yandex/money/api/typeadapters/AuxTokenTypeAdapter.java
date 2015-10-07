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
import com.yandex.money.api.methods.AuxToken;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.Error;

import java.lang.reflect.Type;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class AuxTokenTypeAdapter extends BaseTypeAdapter<AuxToken> {

    private static final AuxTokenTypeAdapter INSTANCE = new AuxTokenTypeAdapter();
    private static final String MEMBER_AUX_TOKEN = "aux_token";
    private static final String MEMBER_ERROR = "error";

    private AuxTokenTypeAdapter() {
    }

    public static AuxTokenTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public AuxToken deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return new AuxToken(JsonUtils.getString(object, MEMBER_AUX_TOKEN),
                Error.parse(JsonUtils.getString(object, MEMBER_ERROR)));
    }

    @Override
    public JsonElement serialize(AuxToken src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_AUX_TOKEN, src.auxToken);
        if (src.error != null) {
            object.addProperty(MEMBER_ERROR, src.error.code);
        }
        return object;
    }

    @Override
    protected Class<AuxToken> getType() {
        return AuxToken.class;
    }
}
