package com.yandex.money;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 *
 */
public class Utils {

    public static String getString(JsonObject jsonObject, String fieldName) {
        JsonPrimitive p = jsonObject.getAsJsonPrimitive(fieldName);
        return p != null ? p.getAsString() : null;
    }
}
