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

package com.yandex.money.api.typeadapters.model.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.uicontrols.Control;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Base type adapter for components implementing {@link Control} interface.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ControlTypeAdapter<T extends Control, U extends Control.Builder> extends ComponentTypeAdapter<T, U> {

    private static final String MEMBER_ALERT = "alert";
    private static final String MEMBER_HINT = "hint";
    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_READONLY = "readonly";
    private static final String MEMBER_REQUIRED = "required";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setAlert(getString(src, MEMBER_ALERT));
        builder.setHint(getString(src, MEMBER_HINT));
        builder.setLabel(getString(src, MEMBER_LABEL));

        JsonElement readOnly = src.get(MEMBER_READONLY);
        if (readOnly != null) {
            builder.setReadonly(readOnly.getAsBoolean());
        }

        JsonElement required = src.get(MEMBER_REQUIRED);
        if (required != null) {
            builder.setRequired(required.getAsBoolean());
        }
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_ALERT, src.alert);
        to.addProperty(MEMBER_HINT, src.hint);
        to.addProperty(MEMBER_LABEL, src.label);
        if (src.readonly) {
            to.addProperty(MEMBER_READONLY, true);
        }
        if (!src.required) {
            to.addProperty(MEMBER_REQUIRED, false);
        }
    }
}
