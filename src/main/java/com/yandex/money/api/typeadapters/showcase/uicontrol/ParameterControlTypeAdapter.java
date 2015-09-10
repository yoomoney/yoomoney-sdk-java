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

package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

/**
 * Base type adapter for components implementing {@link ParameterControl} interface.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ParameterControlTypeAdapter<T extends ParameterControl,
        U extends ParameterControl.Builder> extends ControlTypeAdapter<T, U> {

    private final String MEMBER_NAME = "name";
    private final String MEMBER_VALUE = "value";
    private final String MEMBER_AUTOFILL = "value_autofill";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setName(JsonUtils.getString(src, MEMBER_NAME));
        builder.setValue(JsonUtils.getString(src, MEMBER_VALUE));

        JsonElement autofill = src.get(MEMBER_AUTOFILL);

        if (autofill != null) {
            builder.setValueAutoFill(Parameter.AutoFill.parse(autofill.getAsString()));
        }
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_NAME, src.name);
        to.addProperty(MEMBER_VALUE, src.getValue());
        if (src.valueAutoFill != null) {
            to.addProperty(MEMBER_AUTOFILL, src.valueAutoFill.code);
        }
        super.serialize(src, to, context);
    }
}
