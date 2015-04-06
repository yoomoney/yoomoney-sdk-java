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

    /**
     * url to avatar
     */
    public final String url;

    /**
     * avatar change time
     */
    public final DateTime timestamp;

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
