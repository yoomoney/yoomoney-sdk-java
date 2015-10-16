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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Static class for JSON parsing process.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class JsonUtils {

    public static final DateTimeFormatter ISO_FORMATTER = ISODateTimeFormat.dateTimeParser()
            .withOffsetParsed();

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
        Integer integer = getInt(object, memberName);
        checkMandatoryValue(integer, memberName);
        return integer;
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
        Long l = getLong(object, memberName);
        checkMandatoryValue(l, memberName);
        return l;
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
        Boolean bool = getBoolean(object, memberName);
        checkMandatoryValue(bool, memberName);
        return bool;
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
        String string = getString(object, memberName);
        checkMandatoryValue(string, memberName);
        return string;
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
        BigDecimal bigDecimal = getBigDecimal(object, memberName);
        checkMandatoryValue(bigDecimal, memberName);
        return bigDecimal;
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
        DateTime dateTime = getDateTime(object, memberName);
        checkMandatoryValue(dateTime, memberName);
        return dateTime;
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
    public static DateTime getDateTime(JsonObject object, String memberName,
                                       DateTimeFormatter formatter) {
        checkNotNull(formatter, "formatter");
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : DateTime.parse(primitive.getAsString(), formatter);
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
    public static <T> List<T> getMandatoryArray(JsonObject object, String memberName,
                                                TypeAdapter<T> converter) {
        List<T> value = getArray(object, memberName, converter);
        checkMandatoryValue(value, memberName);
        return value;
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
    public static <T> List<T> getArray(JsonObject object, String memberName,
                                       TypeAdapter<T> converter) {

        checkParameters(object, memberName);
        JsonArray array = object.getAsJsonArray(memberName);
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
    public static <T> List<T> getNotNullArray(JsonObject object, String memberName,
                                              TypeAdapter<T> converter) {
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

    private static JsonPrimitive getPrimitiveChecked(JsonObject object, String memberName) {
        checkParameters(object, memberName);
        return object.getAsJsonPrimitive(memberName);
    }

    private static void checkParameters(JsonObject object, String memberName) {
        checkObject(object);
        checkMemberName(memberName);
    }

    private static void checkObject(JsonObject object) {
        checkNotNull(object, "object");
    }

    private static void checkMemberName(String memberName) {
        checkNotNull(memberName, "memberName");
        if (memberName.length() == 0) {
            throw new IllegalArgumentException("Member is an empty string.");
        }
    }

    private static void checkMandatoryValue(Object value, String memberName) {
        checkNotNull(value, "mandatory value \'" + memberName + "\'");
    }
}
