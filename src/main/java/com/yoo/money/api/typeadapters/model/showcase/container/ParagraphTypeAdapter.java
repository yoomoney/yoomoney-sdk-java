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

package com.yoo.money.api.typeadapters.model.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.yoo.money.api.model.showcase.components.TextBlock;
import com.yoo.money.api.model.showcase.components.containers.Paragraph;

/**
 * Type serializer for {@link Paragraph} component container.
 *
 * @author Anton Ermak (support@yoomoney.ru)
 */
public final class ParagraphTypeAdapter extends ContainerTypeAdapter<TextBlock, Paragraph, Paragraph.Builder> {

    private static final ParagraphTypeAdapter INSTANCE = new ParagraphTypeAdapter();

    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_HREF = "href";

    private ParagraphTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static ParagraphTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected JsonElement serializeItem(TextBlock src, JsonSerializationContext context) {
        if (src.getClass() == TextBlock.WithLink.class) {
            JsonObject element = new JsonObject();
            element.addProperty(MEMBER_TYPE, "a");
            element.addProperty(MEMBER_HREF, ((TextBlock.WithLink) src).link);
            element.addProperty(MEMBER_LABEL, src.text);
            return element;
        } else {
            return new JsonPrimitive(src.text);
        }
    }

    @Override
    protected TextBlock deserializeItem(JsonElement src, JsonDeserializationContext context) {
        if (src.isJsonObject()) {
            JsonObject jsonObject = src.getAsJsonObject();
            return new TextBlock.WithLink(jsonObject.get(MEMBER_LABEL).getAsString(),
                    jsonObject.get(MEMBER_HREF).getAsString());
        } else {
            return new TextBlock(src.getAsString());
        }
    }

    @Override
    protected Paragraph.Builder createBuilderInstance() {
        return new Paragraph.Builder();
    }

    @Override
    protected Paragraph createInstance(Paragraph.Builder builder) {
        return builder.create();
    }

    @Override
    public Class<Paragraph> getType() {
        return Paragraph.class;
    }
}
