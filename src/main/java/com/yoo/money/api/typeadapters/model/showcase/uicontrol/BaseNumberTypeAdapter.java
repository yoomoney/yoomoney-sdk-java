/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.typeadapters.model.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yoo.money.api.model.showcase.components.uicontrols.Number;

import static com.yoo.money.api.typeadapters.JsonUtils.getBigDecimal;


/**
 * Base type adapter for subclasses of {@link Number} component.
 *
 * @author Anton Ermak (support@yoomoney.ru)
 */
abstract class BaseNumberTypeAdapter<T extends Number, U extends Number.Builder>
        extends ParameterControlTypeAdapter<T, U> {

    private static final String MEMBER_MAX = "max";
    private static final String MEMBER_MIN = "min";
    private static final String MEMBER_STEP = "step";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setMax(getBigDecimal(src, MEMBER_MAX));
        builder.setMin(getBigDecimal(src, MEMBER_MIN));
        builder.setStep(getBigDecimal(src, MEMBER_STEP));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_MAX, src.max);
        to.addProperty(MEMBER_MIN, src.min);
        to.addProperty(MEMBER_STEP, src.step);
        super.serialize(src, to, context);
    }
}
