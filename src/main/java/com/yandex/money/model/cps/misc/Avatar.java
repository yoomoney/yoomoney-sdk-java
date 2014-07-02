package com.yandex.money.model.cps.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.JsonUtils;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class Avatar {

    private final String url;
    private final DateTime timestamp;

    public Avatar(String url, DateTime timestamp) {
        if (url == null || url.length() == 0) {
            throw new JsonParseException("avatar url is null or empty");
        }
        this.url = url;
        if (timestamp == null) {
            throw new JsonParseException("avatar timestamp is null");
        }
        this.timestamp = timestamp;
    }

    public static Avatar createFromJson(JsonElement json) {
        return buildGson().fromJson(json, Avatar.class);
    }

    public String getUrl() {
        return url;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Avatar.class, new Deserializer())
                .create();
    }

    private static class Deserializer implements JsonDeserializer<Avatar> {
        @Override
        public Avatar deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            return new Avatar(JsonUtils.getMandatoryString(object, "url"),
                    JsonUtils.getMandatoryDateTime(object, "ts"));
        }
    }
}
