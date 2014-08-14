package com.yandex.money.api.methods;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Static class for JSON parsing process.
 *
 * @author vyasevich
 */
public final class JsonUtils {

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
        return primitive == null ? null :
                DateTime.parse(primitive.getAsString(),
                        ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }

    /**
     * Maps JSON object to key-value pairs.
     *
     * @param object JSON object
     * @return map of string key-value pairs
     */
    public static Map<String, String> map(JsonObject object) {
        checkObject(object);
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getAsString());
        }
        return result;
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
