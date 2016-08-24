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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.Avatar;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;

import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryDateTime;
import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryString;

/**
 * Type adapter for {@link Avatar}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class AvatarTypeAdapter extends BaseTypeAdapter<Avatar> {

    private static final AvatarTypeAdapter INSTANCE = new AvatarTypeAdapter();

    private static final String MEMBER_TS = "ts";
    private static final String MEMBER_URL = "url";

    private AvatarTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static AvatarTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public Avatar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return new Avatar(getMandatoryString(object, MEMBER_URL),
                getMandatoryDateTime(object, MEMBER_TS));
    }

    @Override
    public JsonElement serialize(Avatar src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_URL, src.url);
        object.addProperty(MEMBER_TS, src.timestamp.toString());
        return object;
    }

    @Override
    protected Class<Avatar> getType() {
        return Avatar.class;
    }
}
