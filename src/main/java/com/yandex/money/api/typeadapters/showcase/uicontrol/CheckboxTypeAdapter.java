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
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.uicontrols.Checkbox;

/**
 * Type adapter for {@link Checkbox} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class CheckboxTypeAdapter extends ParameterControlTypeAdapter<Checkbox,
        Checkbox.Builder> {

    private static final CheckboxTypeAdapter INSTANCE = new CheckboxTypeAdapter();

    private static final String MEMBER_CHECKED = "checked";

    private CheckboxTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static CheckboxTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void serialize(Checkbox src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_CHECKED, src.checked);
        super.serialize(src, to, context);
    }

    @Override
    protected void deserialize(JsonObject src, Checkbox.Builder builder,
                               JsonDeserializationContext context) {
        builder.setChecked(src.get(MEMBER_CHECKED).getAsBoolean());
        super.deserialize(src, builder, context);
    }

    @Override
    protected Checkbox.Builder createBuilderInstance() {
        return new Checkbox.Builder();
    }

    @Override
    protected Checkbox createInstance(Checkbox.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<Checkbox> getType() {
        return Checkbox.class;
    }
}
