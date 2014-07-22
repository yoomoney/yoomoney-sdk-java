package com.yandex.money.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class UrlEncodedUtils {

    public static Map<String, String> parse(String url) throws URISyntaxException {
        if (Strings.isNullOrEmpty(url)) {
            throw new IllegalArgumentException("redirectUrl is null or empty");
        }

        URI uri = new URI(url);
        String query = uri.getQuery();
        if (query == null) {
            return Collections.unmodifiableMap(new HashMap<String, String>());
        }

        Map<String, String> map = new HashMap<>();
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return Collections.unmodifiableMap(map);
    }
}
