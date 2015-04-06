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
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Digital Goods that can be obtained after payment if available.
 *
 * @see com.yandex.money.api.model.Good
 */
public class DigitalGoods {

    /**
     * not null list of articles
     */
    public final List<Good> article;

    /**
     * not null list of bonuses
     */
    public final List<Good> bonus;

    /**
     * Constructor.
     *
     * @param article main articles
     * @param bonus bonuses
     */
    public DigitalGoods(List<Good> article, List<Good> bonus) {
        if (article == null) {
            throw new NullPointerException("article is null");
        }
        if (bonus == null) {
            throw new NullPointerException("bonus is null");
        }
        this.article = Collections.unmodifiableList(article);
        this.bonus = Collections.unmodifiableList(bonus);
    }

    /**
     * Creates {@link com.yandex.money.api.model.DigitalGoods} from JSON.
     */
    public static DigitalGoods createFromJson(JsonElement element) {
        return buildGson().fromJson(element, DigitalGoods.class);
    }

    @Override
    public String toString() {
        return "DigitalGoods{" +
                "article=" + article +
                ", bonus=" + bonus +
                '}';
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(DigitalGoods.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<DigitalGoods> {
        @Override
        public DigitalGoods deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new DigitalGoods(deserializeGoods(object.getAsJsonArray("article")),
                    deserializeGoods(object.getAsJsonArray("bonus")));
        }

        private List<Good> deserializeGoods(JsonArray array) {
            List<Good> goods = new ArrayList<>();
            if (array != null) {
                for (JsonElement element : array) {
                    goods.add(Good.createFromJson(element));
                }
            }
            return goods;
        }
    }
}
