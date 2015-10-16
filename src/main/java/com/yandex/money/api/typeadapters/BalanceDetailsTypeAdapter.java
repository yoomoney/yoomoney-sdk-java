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
import com.yandex.money.api.model.BalanceDetails;

import java.lang.reflect.Type;

import static com.yandex.money.api.typeadapters.JsonUtils.getBigDecimal;
import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryBigDecimal;

/**
 * Type adapter for {@link BalanceDetails}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class BalanceDetailsTypeAdapter extends BaseTypeAdapter<BalanceDetails> {

    private static final BalanceDetailsTypeAdapter INSTANCE = new BalanceDetailsTypeAdapter();

    private static final String MEMBER_AVAILABLE = "available";
    private static final String MEMBER_BLOCKED = "blocked";
    private static final String MEMBER_DEBT = "debt";
    private static final String MEMBER_DEPOSITION_PENDING = "deposition_pending";
    private static final String MEMBER_HOLD = "hold";
    private static final String MEMBER_TOTAL = "total";

    private BalanceDetailsTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static BalanceDetailsTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public BalanceDetails deserialize(JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        return new BalanceDetails(getMandatoryBigDecimal(object, MEMBER_TOTAL),
                getMandatoryBigDecimal(object, MEMBER_AVAILABLE),
                getBigDecimal(object, MEMBER_DEPOSITION_PENDING),
                getBigDecimal(object, MEMBER_BLOCKED),
                getBigDecimal(object, MEMBER_DEBT),
                getBigDecimal(object, MEMBER_HOLD));
    }

    @Override
    public JsonElement serialize(BalanceDetails src, Type typeOfSrc,
                                 JsonSerializationContext context) {

        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_TOTAL, src.total);
        object.addProperty(MEMBER_AVAILABLE, src.available);
        object.addProperty(MEMBER_DEPOSITION_PENDING, src.depositionPending);
        object.addProperty(MEMBER_BLOCKED, src.blocked);
        object.addProperty(MEMBER_DEBT, src.debt);
        object.addProperty(MEMBER_HOLD, src.hold);
        return object;
    }

    @Override
    protected Class<BalanceDetails> getType() {
        return BalanceDetails.class;
    }
}
