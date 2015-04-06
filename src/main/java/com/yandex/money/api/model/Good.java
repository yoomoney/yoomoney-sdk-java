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

import java.lang.reflect.Type;

/**
 * Describes digital item, that user can obtain when paying for them.
 */
public class Good {

    /**
     * serial number
     */
    public final String serial;

    /**
     * secret
     */
    public final String secret;

    /**
     * merchant article id
     */
    public final String merchantArticleId;

    /**
     * Constructor.
     *
     * @param serial serial number
     * @param secret secret
     * @param merchantArticleId merchant article id
     */
    public Good(String serial, String secret, String merchantArticleId) {
        if (serial == null) {
            throw new NullPointerException("serial is null");
        }
        if (secret == null) {
            throw new NullPointerException("secret is null");
        }
        this.serial = serial;
        this.secret = secret;
        this.merchantArticleId = merchantArticleId;
    }

    /**
     * Creates {@link com.yandex.money.api.model.Good} from JSON.
     */
    public static Good createFromJson(JsonElement json) {
        return buildGson().fromJson(json, Good.class);
    }

    @Override
    public String toString() {
        return "Good{" +
                "serial='" + serial + '\'' +
                ", secret='" + secret + '\'' +
                ", merchantArticleId='" + merchantArticleId + '\'' +
                '}';
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
