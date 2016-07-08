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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yandex.money.api.utils.Common.checkNotEmpty;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Static class for JSON parsing process.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class JsonUtils {

    public static final DateTimeFormatter ISO_FORMATTER = new DateTimeFormatter(
            ISODateTimeFormat.dateTime().getPrinter(),
            ISODateTimeFormat.dateTimeParser().getParser()).withOffsetParsed();

    /**
     * This class contains only static methods.
     */
    private JsonUtils() {
        // disallow instance creation
    }

    /**
     * Gets int value from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return int value
     */
    public static int getMandatoryInt(JsonObject object, String memberName) {
        return checkMandatoryValue(getInt(object, memberName), memberName);
    }

    /**
     * Gets nullable Integer from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link Integer} value
     */
    public static Integer getInt(JsonObject object, String memberName) {
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : primitive.getAsInt();
    }

    /**
     * Gets long value from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return long value
     */
    public static long getMandatoryLong(JsonObject object, String memberName) {
        return checkMandatoryValue(getLong(object, memberName), memberName);
    }

    /**
     * Gets nullable Long from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link Long} value
     */
    public static Long getLong(JsonObject object, String memberName) {
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : primitive.getAsLong();
    }

    /**
     * Gets boolean value from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return boolean value
     */
    public static boolean getMandatoryBoolean(JsonObject object, String memberName) {
        return checkMandatoryValue(getBoolean(object, memberName), memberName);
    }

    /**
     * Gets nullable Boolean from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link Boolean} value
     */
    public static Boolean getBoolean(JsonObject object, String memberName) {
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : primitive.getAsBoolean();
    }

    /**
     * Gets String from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link String} value
     */
    public static String getMandatoryString(JsonObject object, String memberName) {
        return checkMandatoryValue(getString(object, memberName), memberName);
    }

    /**
     * Gets nullable String from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link String} value
     */
    public static String getString(JsonObject object, String memberName) {
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : primitive.getAsString();
    }

    /**
     * Gets BigDecimal from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link java.math.BigDecimal} value
     */
    public static BigDecimal getMandatoryBigDecimal(JsonObject object, String memberName) {
        return checkMandatoryValue(getBigDecimal(object, memberName), memberName);
    }

    /**
     * Gets nullable BigDecimal from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link java.math.BigDecimal} value
     */
    public static BigDecimal getBigDecimal(JsonObject object, String memberName) {
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : primitive.getAsBigDecimal();
    }

    /**
     * Gets DateTime from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link org.joda.time.DateTime} value
     */
    public static DateTime getMandatoryDateTime(JsonObject object, String memberName) {
        return checkMandatoryValue(getDateTime(object, memberName), memberName);
    }


    /**
     * Gets nullable DateTime from a JSON object.
     *
     * @param object json object
     * @param memberName member's name
     * @return {@link org.joda.time.DateTime} value
     */
    public static DateTime getDateTime(JsonObject object, String memberName) {
        return getDateTime(object, memberName, ISO_FORMATTER);
    }

    /**
     * Gets nullable DateTime from a JSON object using formatter.
     *
     * @param object     json object
     * @param memberName member's name
     * @param formatter  {@link org.joda.time.DateTime}'s formatter.
     * @return {@link org.joda.time.DateTime} value
     */
    public static DateTime getDateTime(JsonObject object, String memberName, DateTimeFormatter formatter) {
        String value = getString(object, memberName);
        return value == null ? null : DateTime.parse(value, checkNotNull(formatter, "formatter"));
    }

    /**
     * Gets array from a JSON object. Uses {@link ArrayList} implementation of {@link List}.
     *
     * @param object json object
     * @param memberName member's name
     * @param converter converter
     * @param <T> type of a value in the array
     * @return list of values
     */
    public static <T> List<T> getMandatoryArray(JsonObject object, String memberName, TypeAdapter<T> converter) {
        return checkMandatoryValue(getArray(object, memberName, converter), memberName);
    }

    /**
     * Gets nullable array from a JSON object. Uses {@link ArrayList} implementation of
     * {@link List}.
     *
     * @param object json object
     * @param memberName member's name
     * @param converter converter
     * @param <T> type of a value in the array
     * @return list of values
     */
    public static <T> List<T> getArray(JsonObject object, String memberName, TypeAdapter<T> converter) {
        JsonArray array = checkObject(object)
                .getAsJsonArray(checkMemberName(memberName));
        if (array == null) {
            return null;
        }

        checkNotNull(converter, "converter");
        List<T> result = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            result.add(converter.fromJson(element));
        }
        return result;
    }

    /**
     * Gets array from a JSON object. Uses {@link ArrayList} implementation of {@link List}. If
     * there is no such member in object then returns empty list.
     *
     * @param object     json object
     * @param memberName member's name
     * @param converter  converter
     * @param <T>        type of a value in the array
     * @return list of values
     */
    public static <T> List<T> getNotNullArray(JsonObject object, String memberName, TypeAdapter<T> converter) {
        List<T> array = getArray(object, memberName, converter);
        return array == null ? Collections.<T>emptyList() : array;
    }

    /**
     * Maps JSON object to key-value pairs. If the object contains non-primitive entries they are
     * ignored and {@code null} value added using specified key.
     *
     * @param object JSON object
     * @return map of string key-value pairs
     */
    public static Map<String, String> map(JsonObject object) {
        checkObject(object);
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String value = null;
            if (entry.getValue().isJsonPrimitive()) {
                value = entry.getValue().getAsString();
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * Maps JSON object to key-value pairs. Returns {@link Collections#emptyMap()} in case of
     * nullable field value.
     *
     * @see {@link #map(JsonObject)}
     *
     * @param object JSON object
     * @param memberName member's name
     * @return map of string key-value pairs
     */
    public static Map<String, String> getNotNullMap(JsonObject object, String memberName) {
        JsonElement jsonElement = object.get(memberName);
        if (jsonElement == null) {
            return Collections.emptyMap();
        } else {
            return map(jsonElement.getAsJsonObject());
        }
    }

    /**
     * Build JSON object using provided map. Returns {@code null} if parameter {@code map} is null.
     *
     * @param map key-value pairs
     * @return JSON object
     */
    public static JsonObject toJsonObject(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        JsonObject object = new JsonObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            object.addProperty(entry.getKey(), entry.getValue());
        }
        return object;
    }

    /**
     * Creates JSON array with collection values using provided converter to create JSON elements.
     * Returns {@code null} if parameter {@code map} is null.
     *
     * @param collection the collection
     * @param converter converter for values
     * @param <T> type of value
     * @return JSON array
     */
    public static <T> JsonArray toJsonArray(Collection<T> collection, TypeAdapter<T> converter) {
        if (collection == null) {
            return null;
        }
        checkNotNull(converter, "converter");
        JsonArray array = new JsonArray();
        for (T value : collection) {
            array.add(converter.toJsonTree(value));
        }
        return array;
    }

    /**
     * Gets UTF-8 bytes of JSON element
     *
     * @param element JSON element
     * @return byte array
     */
    public static byte[] getBytes(JsonElement element) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(stream, Charset.forName("UTF-8")));
        GsonProvider.getGson().toJson(checkNotNull(element, "element"), writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
        return stream.toByteArray();
    }

    private static JsonPrimitive getPrimitiveChecked(JsonObject object, String memberName) {
        return checkObject(object)
                .getAsJsonPrimitive(checkMemberName(memberName));
    }

    private static JsonObject checkObject(JsonObject object) {
        return checkNotNull(object, "object");
    }

    private static String checkMemberName(String memberName) {
        return checkNotEmpty(memberName, "memberName");
    }

    private static <T> T checkMandatoryValue(T value, String memberName) {
        return checkNotNull(value, "mandatory value \'" + memberName + "\'");
    }
}
