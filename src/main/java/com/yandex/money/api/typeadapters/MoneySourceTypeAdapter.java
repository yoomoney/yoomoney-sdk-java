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

import com.google.gson.JsonObject;
import com.yandex.money.api.model.MoneySource;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class MoneySourceTypeAdapter {

    private MoneySourceTypeAdapter() {
    }

    static final class Delegate {

        private static final String MEMBER_ID = "id";

        private Delegate() {
        }

        static <T extends MoneySource.Builder> void deserialize(JsonObject object, T builder) {
            checkNotNull(object, "object");
            checkNotNull(builder, "builder");
            builder.setId(getString(object, MEMBER_ID));
        }

        static <T extends MoneySource> void serialize(JsonObject object, T value) {
            checkNotNull(object, "object");
            checkNotNull(value, "builder");
            object.addProperty(MEMBER_ID, value.id);
        }
    }
}
