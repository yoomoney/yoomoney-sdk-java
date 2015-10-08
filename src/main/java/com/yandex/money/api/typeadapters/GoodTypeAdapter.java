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
import com.yandex.money.api.model.Good;

import java.lang.reflect.Type;

import static com.yandex.money.api.methods.JsonUtils.getMandatoryString;
import static com.yandex.money.api.methods.JsonUtils.getString;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class GoodTypeAdapter extends BaseTypeAdapter<Good> {

    private static final GoodTypeAdapter INSTANCE = new GoodTypeAdapter();

    private static final String MEMBER_MERCHANT_ARTICLE_ID = "merchantArticleId";
    private static final String MEMBER_SECRET = "secret";
    private static final String MEMBER_SERIAL = "serial";

    private GoodTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static GoodTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public Good deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return new Good(getMandatoryString(object, MEMBER_SERIAL),
                getMandatoryString(object, MEMBER_SECRET),
                getString(object, MEMBER_MERCHANT_ARTICLE_ID));
    }

    @Override
    public JsonElement serialize(Good src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_SERIAL, src.serial);
        object.addProperty(MEMBER_SECRET, src.secret);
        object.addProperty(MEMBER_MERCHANT_ARTICLE_ID, src.merchantArticleId);
        return null;
    }

    @Override
    protected Class<Good> getType() {
        return Good.class;
    }
}
