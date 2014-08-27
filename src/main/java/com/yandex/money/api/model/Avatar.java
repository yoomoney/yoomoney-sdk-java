package com.yandex.money.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.JsonUtils;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * Describes avatar from {@link com.yandex.money.api.methods.AccountInfo}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Avatar {

    private final String url;
    private final DateTime timestamp;

    /**
     * Constructor.
     *
     * @param url url to avatar
     * @param timestamp avatar change time
     */
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

    @Override
    public String toString() {
        return "Avatar{" +
                "url='" + url + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * Creates {@link com.yandex.money.api.model.Avatar} from a JSON object.
     *
     * @param json JSON
     * @return {@link com.yandex.money.api.model.Avatar}
     */
    public static Avatar createFromJson(JsonElement json) {
        return buildGson().fromJson(json, Avatar.class);
    }

    /**
     * @return url to avatar
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return avatar change time
     */
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
