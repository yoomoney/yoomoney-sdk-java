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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.ShowcaseSearch;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;
import java.util.List;

import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryArray;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.typeadapters.JsonUtils.toJsonArray;

/**
 * Type adapter for {@link ShowcaseSearch}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseSearchTypeAdapter extends BaseTypeAdapter<ShowcaseSearch> {

    public static final ShowcaseSearchTypeAdapter INSTANCE = new ShowcaseSearchTypeAdapter();

    private static final String MEMBER_ERROR = "error";
    private static final String MEMBER_NEXT_PAGE = "nextPage";
    private static final String MEMBER_RESULT = "result";

    /**
     * @return instance of this class
     */
    public static ShowcaseSearchTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public ShowcaseSearch deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        Error error = Error.parse(getString(object, MEMBER_ERROR));
        if (error == null) {
            List<ShowcaseReference> result = getMandatoryArray(object, MEMBER_RESULT,
                    ShowcaseReferenceTypeAdapter.getInstance());
            return ShowcaseSearch.success(result, getString(object, MEMBER_NEXT_PAGE));
        } else {
            return ShowcaseSearch.failure(error);
        }
    }

    @Override
    public JsonElement serialize(ShowcaseSearch src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        Error error = src.error;
        if (error == null) {
            JsonArray array = toJsonArray(src.result,
                    ShowcaseReferenceTypeAdapter.getInstance());
            object.add(MEMBER_RESULT, array);
            object.addProperty(MEMBER_NEXT_PAGE, src.nextPage);
        } else {
            object.addProperty(MEMBER_ERROR, error.code);
        }
        return object;
    }

    @Override
    protected Class<ShowcaseSearch> getType() {
        return ShowcaseSearch.class;
    }
}
