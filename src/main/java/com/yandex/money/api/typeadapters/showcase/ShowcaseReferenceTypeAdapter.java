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

package com.yandex.money.api.typeadapters.showcase;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Type adapter for {@link ShowcaseReference}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseReferenceTypeAdapter extends BaseTypeAdapter<ShowcaseReference> {

    public static final ShowcaseReferenceTypeAdapter INSTANCE = new ShowcaseReferenceTypeAdapter();

    private static final String MEMBER_TITLE = "title";
    private static final String MEMBER_ID = "id";
    private static final String MEMBER_TOP = "top";
    private static final String MEMBER_URL = "url";
    private static final String MEMBER_FORMAT = "format";
    private static final String MEMBER_PARAMS = "params";

    /**
     * @return instance of this class
     */
    public static ShowcaseReferenceTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public ShowcaseReference deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonObject paramsObject = object.getAsJsonObject(MEMBER_PARAMS);
        Map<String, String> params = paramsObject == null ? new HashMap<String, String>() :
                JsonUtils.map(paramsObject);
        return new ShowcaseReference(JsonUtils.getMandatoryLong(object, MEMBER_ID),
                JsonUtils.getMandatoryString(object, MEMBER_TITLE),
                JsonUtils.getInt(object, MEMBER_TOP),
                JsonUtils.getString(object, MEMBER_URL),
                ShowcaseReference.Format.parse(JsonUtils.getString(object, MEMBER_FORMAT)),
                params);
    }

    @Override
    public JsonElement serialize(ShowcaseReference src, Type typeOfSrc, JsonSerializationContext
            context) {
        JsonObject object = new JsonObject();
        if (!src.params.equals(Collections.emptyMap())) {
            object.add(MEMBER_PARAMS, JsonUtils.toJsonObject(src.params));
        }
        object.addProperty(MEMBER_ID, src.scid);
        object.addProperty(MEMBER_TITLE, src.title);
        object.addProperty(MEMBER_TOP, src.topIndex);
        object.addProperty(MEMBER_URL, src.url);
        if (src.format != ShowcaseReference.Format.UNKNOWN) {
            object.addProperty(MEMBER_FORMAT, src.format.code);
        }
        return object;
    }

    @Override
    protected Class<ShowcaseReference> getType() {
        return ShowcaseReference.class;
    }
}
