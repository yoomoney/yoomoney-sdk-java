package com.yandex.money.api.net;

/**
 * API responses implement this interface.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface MethodResponse {
    String CODE_SUCCESS = "success";
    String CODE_REFUSED = "refused";
    String CODE_HOLD_FOR_PICKUP = "hold_for_pickup";
    String CODE_IN_PROGRESS = "in_progress";
    String CODE_EXT_AUTH_REQUIRED = "ext_auth_required";
    String CODE_UNKNOWN = "unknown";
}
