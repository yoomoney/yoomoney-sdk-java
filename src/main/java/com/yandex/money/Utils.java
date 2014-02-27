package com.yandex.money;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Utils {

    public static String NULL_PARAM_STR = "parameter %s is null or empty";

    public static String emptyParam(String paramName) {
        return String.format(NULL_PARAM_STR, paramName);
    }

    public static String getString(JsonObject jsonObject, String fieldName) {
        JsonPrimitive p = jsonObject.getAsJsonPrimitive(fieldName);
        return p != null ? p.getAsString() : null;
    }

    public static Long getLong(JsonObject jsonObject, String fieldName) {
        JsonPrimitive p = jsonObject.getAsJsonPrimitive(fieldName);
        return p != null ? p.getAsLong() : null;
    }

    public static Boolean getBoolean(JsonObject jsonObject, String fieldName) {
        JsonPrimitive p = jsonObject.getAsJsonPrimitive(fieldName);
        return p != null ? p.getAsBoolean() : null;
    }

    public static Map<String, String> parse(JsonObject obj) {
        Map<String, String> result = new HashMap<String, String>();

        if (obj != null) {
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                JsonElement el = entry.getValue();
                if (el.isJsonPrimitive()) {
                    result.put(entry.getKey(), el.getAsString());
                }
            }
        }

        return result;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    public static String sumToStr(BigDecimal amountDue) {
        return amountDue.toString();  // @todo make a test for values like 1.929299299
    }
}
