package com.yandex.money.model.cps.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.JsonUtils;

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

    public String getPanFragment() {
        return panFragment;
    }

    public String getType() {
        return type;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Card.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<Card> {
        @Override
        public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new Card(JsonUtils.getString(object, "id"),
                    JsonUtils.getString(object, "pan_fragment"),
                    JsonUtils.getString(object, "type"));
        }
    }
}
