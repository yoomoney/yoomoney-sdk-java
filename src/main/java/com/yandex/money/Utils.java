package com.yandex.money;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 *
 */
public class Utils {

    static String NULL_PARAM_STR = "parameter %s is null or empty";

    public static String emptyParam(String paramName) {
        return String.format(NULL_PARAM_STR, paramName);
    }

    public static String getString(JsonObject jsonObject, String fieldName) {
        JsonPrimitive p = jsonObject.getAsJsonPrimitive(fieldName);
        return p != null ? p.getAsString() : null;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
