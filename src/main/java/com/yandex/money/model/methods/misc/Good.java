package com.yandex.money.model.methods.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.methods.JsonUtils;

import java.lang.reflect.Type;

public class Good {

    private final String serial;
    private final String secret;
    private final String merchantArticleId;

    public Good(String serial, String secret, String merchantArticleId) {
        if (serial == null) {
            throw new NullPointerException("serial is null");
        }
        this.serial = serial;
        if (secret == null) {
            throw new NullPointerException("secret is null");
        }
        this.secret = secret;
        this.merchantArticleId = merchantArticleId;
    }

    public static Good createFromJson(JsonElement json) {
        return buildGson().fromJson(json, Good.class);
    }

    public String getSerial() {
        return serial;
    }

    public String getSecret() {
        return secret;
    }

    public String getMerchantArticleId() {
        return merchantArticleId;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Good.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<Good> {
        @Override
        public Good deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            return new Good(JsonUtils.getMandatoryString(object, "serial"),
                    JsonUtils.getMandatoryString(object, "secret"),
                    JsonUtils.getString(object, "merchantArticleId"));
        }
    }
}
