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
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yandex.money.api.typeadapters.JsonUtils.getInt;
import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryLong;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.typeadapters.JsonUtils.map;
import static com.yandex.money.api.typeadapters.JsonUtils.toJsonObject;

/**
 * Type adapter for {@link ShowcaseReference}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseReferenceTypeAdapter extends BaseTypeAdapter<ShowcaseReference> {

    public static final ShowcaseReferenceTypeAdapter INSTANCE = new ShowcaseReferenceTypeAdapter();

    private static final String MEMBER_FORMAT = "format";
    private static final String MEMBER_ID = "id";
    private static final String MEMBER_PARAMS = "params";
    private static final String MEMBER_TITLE = "title";
    private static final String MEMBER_TOP = "top";
    private static final String MEMBER_URL = "url";

    /**
     * @return instance of this class
     */
    public static ShowcaseReferenceTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public ShowcaseReference deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        JsonObject paramsObject = object.getAsJsonObject(MEMBER_PARAMS);
        Map<String, String> params = paramsObject == null ? new HashMap<String, String>() : map(paramsObject);
        return new ShowcaseReference.Builder()
                .setScid(getMandatoryLong(object, MEMBER_ID))
                .setTitle(getString(object, MEMBER_TITLE))
                .setTopIndex(getInt(object, MEMBER_TOP))
                .setUrl(getString(object, MEMBER_URL))
                .setFormat(ShowcaseReference.Format.parse(getString(object, MEMBER_FORMAT)))
                .setParams(params)
                .create();
    }

    @Override
    public JsonElement serialize(ShowcaseReference src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        if (!src.params.equals(Collections.emptyMap())) {
            object.add(MEMBER_PARAMS, toJsonObject(src.params));
        }
        object.addProperty(MEMBER_ID, src.scid);
        object.addProperty(MEMBER_TITLE, src.title);
        object.addProperty(MEMBER_TOP, src.topIndex);
        object.addProperty(MEMBER_URL, src.url);
        if (src.format != null) {
            object.addProperty(MEMBER_FORMAT, src.format.code);
        }
        return object;
    }

    @Override
    public Class<ShowcaseReference> getType() {
        return ShowcaseReference.class;
    }
}
