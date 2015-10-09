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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.uicontrols.Select;
import com.yandex.money.api.typeadapters.showcase.container.GroupTypeAdapter.ListDelegate;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Type adapter for {@link @Select} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class SelectTypeAdapter extends ParameterControlTypeAdapter<Select, Select.Builder> {

    private static final SelectTypeAdapter INSTANCE = new SelectTypeAdapter();
    private static final String MEMBER_GROUP = "group";
    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_OPTIONS = "options";
    private static final String MEMBER_STYLE = "style";
    private static final String MEMBER_VALUE = "value";

    private SelectTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static SelectTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void deserialize(JsonObject src, Select.Builder builder,
                               JsonDeserializationContext context) {
        for (JsonElement item : src.getAsJsonArray(MEMBER_OPTIONS)) {
            JsonObject itemObject = item.getAsJsonObject();

            Select.Option option = new Select.Option(itemObject.get(MEMBER_LABEL).getAsString(),
                    itemObject.get(MEMBER_VALUE).getAsString());
            if (itemObject.has(MEMBER_GROUP)) {
                option.group = ListDelegate.deserialize(
                        itemObject.getAsJsonArray(MEMBER_GROUP), context);
            }
            builder.addOption(option);
        }
        builder.setStyle(Select.Style.parse(getString(src, MEMBER_STYLE)));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(Select src, JsonObject to, JsonSerializationContext context) {
        Select.Option selectedOption = src.getSelectedOption();

        if (selectedOption != null) {
            to.addProperty(MEMBER_VALUE, selectedOption.value);
        }
        if (src.style != null) {
            to.addProperty(MEMBER_STYLE, src.style.code);
        }

        JsonArray options = new JsonArray();
        for (Select.Option option : src.options) {
            JsonObject optionElement = new JsonObject();

            optionElement.addProperty(MEMBER_LABEL, option.label);
            optionElement.addProperty(MEMBER_VALUE, option.value);

            if (option.group != null) {
                optionElement.add(MEMBER_GROUP, ListDelegate.serialize(option.group, context));
            }
            options.add(optionElement);
        }
        to.add(MEMBER_OPTIONS, options);
        super.serialize(src, to, context);
    }

    @Override
    protected Select.Builder createBuilderInstance() {
        return new Select.Builder();
    }

    @Override
    protected Select createInstance(Select.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<Select> getType() {
        return Select.class;
    }
}
