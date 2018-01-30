package com.yandex.money.api.util;

import com.yandex.money.api.net.ApiRequest.Method;

public final class HttpMethod {
    private HttpMethod() {
    }

    /**
     * Checks if method supports request body
     *
     * @return {@code true} if supports
     */
    public static boolean supportsRequestBody(Method method) {
        return method != Method.GET && method != Method.DELETE;
    }
}
