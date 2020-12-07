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
import com.yoo.money.api.model.showcase.components.uicontrols.Date;
import com.yoo.money.api.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;

import static com.yoo.money.api.typeadapters.JsonUtils.getString;

/**
 * Base type adapter for subclasses of {@link Date} component.
 *
 * @author Anton Ermak (support@yoomoney.ru)
 * @author Slava Yasevich
 */
abstract class BaseDateTypeAdapter<T extends Date, U extends Date.Builder> extends ParameterControlTypeAdapter<T, U> {

    private static final String MEMBER_MAX = "max";
    private static final String MEMBER_MIN = "min";

    @Override
    protected final void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        try {
            builder.setMin(parseDate(src, MEMBER_MIN));
        } catch (ParseException e) {
            // ignore restriction
        }
        try {
            builder.setMax(parseDate(src, MEMBER_MAX));
        } catch (ParseException e) {
            // ignore restriction
        }
        super.deserialize(src, builder, context);
    }

    @Override
    protected final void serialize(T src, JsonObject to, JsonSerializationContext context) {
        if(src.min != null) {
            to.addProperty(MEMBER_MIN, src.min.toString(getFormatter()));
        }
        if(src.max != null) {
            to.addProperty(MEMBER_MAX, src.max.toString(getFormatter()));
        }
        super.serialize(src, to, context);
    }

    protected DateFormat getFormatter() {
        return Date.FORMATTER;
    }

    private DateTime parseDate(JsonObject src, String memberName) throws ParseException {
        return Date.parseDate(getString(src, memberName), getFormatter());
    }
}
