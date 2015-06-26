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

package com.yandex.money.api.methods;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yandex.money.api.typeadapters.TypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static class for JSON parsing process.
 *
 * Note: in upcoming major release this class will become package local and will be moved to
 * {@code com.yandex.money.api.typeadapters} package.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class JsonUtils { // TODO read note above and do the stuff in future release

    private static final DateTimeFormatter ISO_FORMATTER = ISODateTimeFormat.dateTimeParser()
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
        JsonPrimitive primitive = getPrimitiveChecked(object, memberName);
        return primitive == null ? null : DateTime.parse(primitive.getAsString(), ISO_FORMATTER);
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

        if (converter == null) {
            throw new NullPointerException("converter is null");
        }
        List<T> result = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            result.add(converter.fromJson(element));
        }
        return result;
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
        if (converter == null) {
            throw new NullPointerException("converter is null");
        }
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
        if (object == null) {
            throw new NullPointerException("JSON object is null.");
        }
    }

    private static void checkMemberName(String memberName) {
        if (memberName == null) {
            throw new NullPointerException("Member name is null.");
        }
        if (memberName.length() == 0) {
            throw new IllegalArgumentException("Member is an empty string.");
        }
    }

    private static void checkMandatoryValue(Object value, String memberName) {
        if (value == null) {
            throw new NullPointerException("mandatory value \'" + memberName + "\' is null");
        }
    }
}
