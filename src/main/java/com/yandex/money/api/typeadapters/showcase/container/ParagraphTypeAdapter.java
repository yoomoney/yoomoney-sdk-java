package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.TextBlock;
import com.yandex.money.api.model.showcase.components.container.Paragraph;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public class ParagraphTypeAdapter extends ContainerTypeAdapter<TextBlock, Paragraph, Paragraph
        .Builder> {

    private static final String KEY_LABEL = "label";
    private static final String KEY_HREF = "href";

    @Override
    protected JsonElement serializeItem(TextBlock item, JsonSerializationContext context) {
        if (item instanceof TextBlock.WithLink) {
            JsonObject element = new JsonObject();
            element.addProperty("type", "a");
            element.addProperty("href", ((TextBlock.WithLink) item).link);
            element.addProperty("label", item.content);
            return element;
        } else {
            return new JsonPrimitive(item.content);
        }
    }

    @Override
    protected TextBlock deserializeItem(JsonElement json, JsonDeserializationContext context) {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            return new TextBlock.WithLink(jsonObject.get(KEY_LABEL).getAsString(),
                    jsonObject.get(KEY_HREF).getAsString());
        } else {
            return new TextBlock(json.getAsString());
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
