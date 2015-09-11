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

package com.yandex.money.api.typeadapters;

import com.google.gson.JsonElement;

import java.io.InputStream;

/**
 * Serializes and deserializes object to and from JSON.
 *
 * @param <T> type of object
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface TypeAdapter<T> {

    /**
     * Creates object from json.
     *
     * @param json json string
     * @return object
     */
    T fromJson(String json);

    /**
     * Creates object from {@link InputStream}.
     *
     * @param inputStream input stream
     * @return object
     */
    T fromJson(InputStream inputStream);

    /**
     * Creates object from json element.
     *
     * @param element json element
     * @return object
     */
    T fromJson(JsonElement element);

    /**
     * Serializes object to json string.
     *
     * @param value object
     * @return json string
     */
    String toJson(T value);

    /**
     * Serializes object to json element.
     *
     * @param value object
     * @return json element
     */
    JsonElement toJsonTree(T value);
}
