package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.TextBlock;
import com.yandex.money.api.model.showcase.components.container.Paragraph;

/**
 * Type serializer for {@link Paragraph} component container.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ParagraphTypeAdapter
        extends ContainerTypeAdapter<TextBlock, Paragraph, Paragraph.Builder> {

    public static final ParagraphTypeAdapter INSTANCE = new ParagraphTypeAdapter();

    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_HREF = "href";

    private ParagraphTypeAdapter() {
    }

    @Override
    protected JsonElement serializeItem(TextBlock src, JsonSerializationContext context) {
        if (src instanceof TextBlock.WithLink) {
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
}
