package com.yandex.money.model.methods.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yandex.money.model.methods.JsonUtils;

import java.lang.reflect.Type;

/**
 * @author vyasevich
 */
public class Card extends MoneySource {

    private final String panFragment;
    private final String type;

    public Card(String id, String panFragment, String type) {
        super(id);
        this.panFragment = panFragment;
        this.type = type;
    }

    public static Card createFromJson(JsonElement element) {
        return buildGson().fromJson(element, Card.class);
    }

    public static Card createFromJson(String json) {
        return buildGson().fromJson(json, Card.class);
    }

    public String getPanFragment() {
        return panFragment;
    }

    public String getType() {
        return type;
    }

    public String serializeToJson() {
        return buildGson().toJson(this);
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Card.class, new TypeAdapter())
                .create();
    }

    private static final class TypeAdapter
            implements JsonDeserializer<Card>, JsonSerializer<Card> {

        private static final String FIELD_ID = "id";
        private static final String FIELD_PAN_FRAGMENT = "pan_fragment";
        private static final String FIELD_TYPE = "type";

        @Override
        public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new Card(JsonUtils.getString(object, FIELD_ID),
                    JsonUtils.getString(object, FIELD_PAN_FRAGMENT),
                    JsonUtils.getString(object, FIELD_TYPE));
        }

        @Override
        public JsonElement serialize(Card src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty(FIELD_ID, src.getId());
            object.addProperty(FIELD_PAN_FRAGMENT, src.getPanFragment());
            object.addProperty(FIELD_TYPE, src.getType());
            return null;
        }
    }
}
