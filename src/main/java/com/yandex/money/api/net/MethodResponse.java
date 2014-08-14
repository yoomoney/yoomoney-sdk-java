package com.yandex.money.api.net;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface MethodResponse {
    static final String CODE_SUCCESS = "success";
    static final String CODE_REFUSED = "refused";
    static final String CODE_HOLD_FOR_PICKUP = "hold_for_pickup";
    static final String CODE_IN_PROGRESS = "in_progress";
    static final String CODE_EXT_AUTH_REQUIRED = "ext_auth_required";
    static final String CODE_UNKNOWN = "unknown";
}
