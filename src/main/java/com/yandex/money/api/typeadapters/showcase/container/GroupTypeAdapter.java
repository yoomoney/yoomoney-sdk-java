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

package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.typeadapters.showcase.ComponentsTypeProvider;
import com.yandex.money.api.typeadapters.showcase.uicontrol.AmountTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.CheckboxTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.DateTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.EmailTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.MonthTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.NumberTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.SelectTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.SubmitTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.TelTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.TextAreaTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.TextTypeAdapter;

/**
 * Type serializer for {@link Group} component container.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class GroupTypeAdapter extends ContainerTypeAdapter<Component, Group, Group.Builder> {

    private static final GroupTypeAdapter INSTANCE = new GroupTypeAdapter();

    private static final String MEMBER_LAYOUT = "layout";

    static {
        // register type adapters to GSON instance.
        NumberTypeAdapter.getInstance();
        AmountTypeAdapter.getInstance();
        CheckboxTypeAdapter.getInstance();
        DateTypeAdapter.getInstance();
        EmailTypeAdapter.getInstance();
        MonthTypeAdapter.getInstance();
        SelectTypeAdapter.getInstance();
        SubmitTypeAdapter.getInstance();
        TextAreaTypeAdapter.getInstance();
        TextTypeAdapter.getInstance();
        TelTypeAdapter.getInstance();
        ParagraphTypeAdapter.getInstance();
    }

    private GroupTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static GroupTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void deserialize(JsonObject src, Group.Builder builder,
                               JsonDeserializationContext context) {

        JsonElement layout = src.getAsJsonPrimitive(MEMBER_LAYOUT);
        if (layout != null) {
            builder.setLayout(Group.Layout.parse(layout.getAsString()));
        }
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(Group src, JsonObject to, JsonSerializationContext context) {
        if (src.layout != Group.Layout.VERTICAL) {
            to.addProperty(MEMBER_LAYOUT, src.layout.code);
        }
        super.serialize(src, to, context);
    }

    @Override
    protected Group.Builder createBuilderInstance() {
        return new Group.Builder();
    }

    @Override
    protected Group createInstance(Group.Builder builder) {
        return builder.create();
    }

    @Override
    protected JsonElement serializeItem(Component src, JsonSerializationContext context) {
        return context.serialize(src);
    }

    @Override
    protected Component deserializeItem(JsonElement src, JsonDeserializationContext context) {
        return context.deserialize(src, ComponentsTypeProvider.getJsonComponentType(src));
    }

    @Override
    protected Class<Group> getType() {
        return Group.class;
    }
}
