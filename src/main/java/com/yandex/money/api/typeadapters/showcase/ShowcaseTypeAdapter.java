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
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.model.showcase.Showcase.Error;
import com.yandex.money.api.typeadapters.AllowedMoneySourceTypeAdapter;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.container.GroupTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.container.GroupTypeAdapter.ListDelegate;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseTypeAdapter extends BaseTypeAdapter<Showcase> {

    private static final ShowcaseTypeAdapter INSTANCE = new ShowcaseTypeAdapter();

    private static final String ELEMENT_TITLE = "title";
    private static final String ELEMENT_HIDDEN_FIELDS = "hidden_fields";
    private static final String ELEMENT_FORM = "form";
    private static final String ELEMENT_MONEY_SOURCE = "money_source";
    private static final String ELEMENT_ERROR = "error";

    static {
        // register type adapters to GSON instance.
        GroupTypeAdapter.getInstance();
    }

    private ShowcaseTypeAdapter() {
    }

    public static ShowcaseTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public Showcase deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

        JsonObject root = json.getAsJsonObject();

        return new Showcase.Builder()
                .setTitle(JsonUtils.getMandatoryString(root, ELEMENT_TITLE))
                .setHiddenFields(JsonUtils.map(root.get(ELEMENT_HIDDEN_FIELDS).getAsJsonObject()))
                .setForm(ListDelegate.deserialize(root.getAsJsonArray(ELEMENT_FORM), context))
                .setMoneySources(new LinkedHashSet<>(JsonUtils.getArray(root, ELEMENT_MONEY_SOURCE,
                        AllowedMoneySourceTypeAdapter.INSTANCE)))
                .setErrors(JsonUtils.getArray(root, ELEMENT_ERROR, ErrorTypeAdapter.INSTANCE))
                .create();
    }

    @Override
    public JsonElement serialize(Showcase src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();

        root.addProperty(ELEMENT_TITLE, src.title);
        root.add(ELEMENT_MONEY_SOURCE, JsonUtils.toJsonArray(src.moneySources,
                AllowedMoneySourceTypeAdapter.INSTANCE));
        if (!src.errors.isEmpty()) {
            root.add(ELEMENT_ERROR, JsonUtils.toJsonArray(src.errors, ErrorTypeAdapter.INSTANCE));
        }
        root.add(ELEMENT_FORM, ListDelegate.serialize(src.form, context));
        root.add(ELEMENT_HIDDEN_FIELDS, JsonUtils.toJsonObject(src.hiddenFields));
        return root;
    }

    @Override
    protected Class<Showcase> getType() {
        return Showcase.class;
    }

    private static final class ErrorTypeAdapter extends BaseTypeAdapter<Showcase.Error> {

        public static final ErrorTypeAdapter INSTANCE = new ErrorTypeAdapter();

        private static final String MEMBER_NAME = "name";
        private static final String MEMBER_ALERT = "alert";

        @Override
        public Error deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();
            return new Error(JsonUtils.getMandatoryString(jsonObject, MEMBER_NAME),
                    JsonUtils.getMandatoryString(jsonObject, MEMBER_ALERT));
        }

        @Override
        public JsonElement serialize(Error src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", src.name);
            jsonObject.addProperty("alert", src.alert);
            return jsonObject;
        }

        @Override
        protected Class<Error> getType() {
            return Showcase.Error.class;
        }
    }
}
