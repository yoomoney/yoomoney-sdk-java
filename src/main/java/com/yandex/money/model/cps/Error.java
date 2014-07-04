package com.yandex.money.model.cps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Error {

    ACCOUNT_BLOCKED("account_blocked"),
    AUTHORIZATION_REJECT("authorization_reject"),
    EXT_ACTION_REQUIRED("ext_action_required"),
    ILLEGAL_PARAM_AMOUNT("illegal_param_amount"),
    ILLEGAL_PARAM_AMOUNT_DUE("illegal_param_amount_due"),
    ILLEGAL_PARAM_CLIENT_ID("illegal_param_client_id"),
    ILLEGAL_PARAM_COMMENT("illegal_param_comment"),
    ILLEGAL_PARAM_CSC("illegal_param_csc"),
    ILLEGAL_PARAM_EXPIRE_PERIOD("illegal_param_expire_period"),
    ILLEGAL_PARAM_EXT_AUTH_FAIL_URI("illegal_param_ext_auth_fail_uri"),
    ILLEGAL_PARAM_EXT_AUTH_SUCCESS_URI("illegal_param_ext_auth_success_uri"),
    ILLEGAL_PARAM_FROM("illegal_param_from"),
    ILLEGAL_PARAM_INSTANCE_ID("illegal_param_instance_id"),
    ILLEGAL_PARAM_LABEL("illegal_param_label"),
    ILLEGAL_PARAM_MESSAGE("illegal_param_message"),
    ILLEGAL_PARAM_MONEY_SOURCE_TOKEN("illegal_param_money_source_token"),
    ILLEGAL_PARAM_RECORDS("illegal_param_records"),
    ILLEGAL_PARAM_REQUEST_ID("illegal_param_request_id"),
    ILLEGAL_PARAM_START_RECORD("illegal_param_start_record"),
    ILLEGAL_PARAM_TO("illegal_param_to"),
    ILLEGAL_PARAM_TYPE("illegal_param_type"),
    ILLEGAL_PARAM_TILL("illegal_param_till"),
    ILLEGAL_PARAMS("illegal_params"),
    NOT_ENOUGH_FUNDS("not_enough_funds"),
    LIMIT_EXCEEDED("limit_exceeded"),
    PAYEE_NOT_FOUND("payee_not_found"),
    PAYMENT_REFUSED("payment_refused"),
    UNKNOWN("unknown");


    private static final Map<String, Error> ERRORS;
    static {
        Map<String, Error> map = new HashMap<>();
        for (Error value : values()) {
            map.put(value.error, value);
        }
        ERRORS = Collections.unmodifiableMap(map);
    }

    private final String error;

    private Error(String error) {
        this.error = error;
    }

    public static Error parse(String error) {
        return ERRORS.containsKey(error) ? ERRORS.get(error) : UNKNOWN;
    }
}
