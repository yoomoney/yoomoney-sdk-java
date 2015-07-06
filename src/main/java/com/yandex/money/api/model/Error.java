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

package com.yandex.money.api.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * List of errors you may encounter when using API methods.
 */
public enum Error {

    ACCESS_DENIED("access_denied"),
    ACCOUNT_BLOCKED("account_blocked"),
    ALREADY_ACCEPTED("already_accepted"),
    ALREADY_REJECTED("already_rejected"),
    AUTHORIZATION_REJECT("authorization_reject"),
    CONTRACT_NOT_FOUND("contract_not_found"),
    DATA_EXPIRED("data_expired"),
    EXT_ACTION_REQUIRED("ext_action_required"),
    FAVORITE_DUPLICATE("favourite_duplicate"),
    ILLEGAL_PARAM_AMOUNT("illegal_param_amount"),
    ILLEGAL_PARAM_AMOUNT_DUE("illegal_param_amount_due"),
    ILLEGAL_PARAM_APPLICATION_NAME("illegal_param_application_name"),
    ILLEGAL_PARAM_CLIENT_ID("illegal_param_client_id"),
    ILLEGAL_PARAM_COMMENT("illegal_param_comment"),
    ILLEGAL_PARAM_CSC("illegal_param_csc"),
    ILLEGAL_PARAM_DRIVER_LICENSE("illegal_param_driver_license"),
    ILLEGAL_PARAM_EXPIRE_PERIOD("illegal_param_expire_period"),
    ILLEGAL_PARAM_EXT_AUTH_FAIL_URI("illegal_param_ext_auth_fail_uri"),
    ILLEGAL_PARAM_EXT_AUTH_SUCCESS_URI("illegal_param_ext_auth_success_uri"),
    ILLEGAL_PARAM_FAVORITE_ID("illegal_param_favourite_id"),
    ILLEGAL_PARAM_FROM("illegal_param_from"),
    ILLEGAL_PARAM_INSTANCE_ID("illegal_param_instance_id"),
    ILLEGAL_PARAM_LABEL("illegal_param_label"),
    ILLEGAL_PARAM_MESSAGE("illegal_param_message"),
    ILLEGAL_PARAM_MONEY_SOURCE_TOKEN("illegal_param_money_source_token"),
    ILLEGAL_PARAM_NOTIFICATION_CLIENT_TYPE("illegal_param_notification_client_type"),
    ILLEGAL_PARAM_NOTIFICATION_TOKEN("illegal_param_notification_token"),
    ILLEGAL_PARAM_OPERATION_ID("illegal_param_operation_id"),
    ILLEGAL_PARAM_PROTECTION_CODE("illegal_param_protection_code"),
    ILLEGAL_PARAM_QUERY("illegal_param_query"),
    ILLEGAL_PARAM_RECORDS("illegal_param_records"),
    ILLEGAL_PARAM_REQUEST_ID("illegal_param_request_id"),
    ILLEGAL_PARAM_START_RECORD("illegal_param_start_record"),
    ILLEGAL_PARAM_TILL("illegal_param_till"),
    ILLEGAL_PARAM_TITLE("illegal_param_title"),
    ILLEGAL_PARAM_TO("illegal_param_to"),
    ILLEGAL_PARAM_TYPE("illegal_param_type"),
    ILLEGAL_PARAM_UUID("illegal_param_uuid"),
    ILLEGAL_PARAM_VEHICLE_REG_CERTIFICATE("illegal_param_vehicle_reg_certificate"),
    ILLEGAL_PARAMS("illegal_params"),
    INVALID_GRANT("invalid_grant"),
    INVALID_IMAGE("invalid_image"),
    INVALID_REQUEST("invalid_request"),
    INVALID_SCOPE("invalid_scope"),
    NOT_ENOUGH_FUNDS("not_enough_funds"),
    LIMIT_EXCEEDED("limit_exceeded"),
    MONEY_SOURCE_NOT_AVAILABLE("money_source_not_available"),
    PAYEE_NOT_FOUND("payee_not_found"),
    PAYMENT_EXPIRED("payment_expired"),
    PAYMENT_REFUSED("payment_refused"),
    TECHNICAL_ERROR("technical_error"),
    TOO_MANY_RECORDS("too_many_records"),
    UNAUTHORIZED_CLIENT("unauthorized_client"),
    UNKNOWN("unknown");

    private static final Map<String, Error> ERRORS;
    static {
        Map<String, Error> map = new HashMap<>();
        for (Error value : values()) {
            map.put(value.code, value);
        }
        ERRORS = Collections.unmodifiableMap(map);
    }

    public final String code;

    Error(String code) {
        this.code = code;
    }

    public static Error parse(String error) {
        if (error == null) {
            return null;
        }
        return ERRORS.containsKey(error) ? ERRORS.get(error) : UNKNOWN;
    }
}
