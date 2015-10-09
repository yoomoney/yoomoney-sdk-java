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
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.model.Error;

import java.lang.reflect.Type;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Type adapter for {@link InstanceId}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class InstanceIdTypeAdapter extends BaseTypeAdapter<InstanceId> {

    private static final InstanceIdTypeAdapter INSTANCE = new InstanceIdTypeAdapter();

    private static final String MEMBER_ERROR = "error";
    private static final String MEMBER_INSTANCE_ID = "instance_id";
    private static final String MEMBER_STATUS = "status";

    private InstanceIdTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static InstanceIdTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public InstanceId deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject o = json.getAsJsonObject();
        return new InstanceId(
                InstanceId.Status.parse(getString(o, MEMBER_STATUS)),
                Error.parse(getString(o, MEMBER_ERROR)),
                getString(o, MEMBER_INSTANCE_ID));
    }

    @Override
    public JsonElement serialize(InstanceId src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_STATUS, src.status.code);
        object.addProperty(MEMBER_INSTANCE_ID, src.instanceId);

        if (src.error != null) {
            object.addProperty(MEMBER_ERROR, src.error.code);
        }
        return object;
    }

    @Override
    protected Class<InstanceId> getType() {
        return InstanceId.class;
    }
}
