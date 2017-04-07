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

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * List of errors you may encounter when using API methods.
 */
public enum Error {

    @SerializedName("access_denied")
    ACCESS_DENIED("access_denied"),
    @SerializedName("account_blocked")
    ACCOUNT_BLOCKED("account_blocked"),
    @SerializedName("activation_refused")
    ACTIVATION_REFUSED("activation_refused"),
    @SerializedName("activation_too_many_ycard")
    ACTIVATION_TOO_MANY_YCARD("activation_too_many_ycard"),
    @SerializedName("already_accepted")
    ALREADY_ACCEPTED("already_accepted"),
    @SerializedName("already_exists")
    ALREADY_EXISTS("already_exists"),
    @SerializedName("already_rejected")
    ALREADY_REJECTED("already_rejected"),
    @SerializedName("application_blocked")
    APPLICATION_BLOCKED("application_blocked"),
    @SerializedName("authorization_reject")
    AUTHORIZATION_REJECT("authorization_reject"),
    @SerializedName("contract_not_found")
    CONTRACT_NOT_FOUND("contract_not_found"),
    @SerializedName("data_expired")
    DATA_EXPIRED("data_expired"),
    @SerializedName("ext_action_required")
    EXT_ACTION_REQUIRED("ext_action_required"),
    @SerializedName("favourite_duplicate")
    FAVORITE_DUPLICATE("favourite_duplicate"),
    @SerializedName("illegal_param_activation_code")
    ILLEGAL_PARAM_ACTIVATION_CODE("illegal_param_activation_code"),
    @SerializedName("illegal_param_amount")
    ILLEGAL_PARAM_AMOUNT("illegal_param_amount"),
    @SerializedName("illegal_param_amount_due")
    ILLEGAL_PARAM_AMOUNT_DUE("illegal_param_amount_due"),
    @SerializedName("illegal_param_application_name")
    ILLEGAL_PARAM_APPLICATION_NAME("illegal_param_application_name"),
    @SerializedName("illegal_param_client_id")
    ILLEGAL_PARAM_CLIENT_ID("illegal_param_client_id"),
    @SerializedName("illegal_param_comment")
    ILLEGAL_PARAM_COMMENT("illegal_param_comment"),
    @SerializedName("illegal_param_contents")
    ILLEGAL_PARAM_CONTENTS("illegal_param_contents"),
    @SerializedName("illegal_param_csc")
    ILLEGAL_PARAM_CSC("illegal_param_csc"),
    @SerializedName("illegal_param_deviceId")
    ILLEGAL_PARAM_DEVICE_ID("illegal_param_deviceId"),
    @SerializedName("illegal_param_digitizedCardId")
    ILLEGAL_PARAM_DIGITIZED_CARD_ID("illegal_param_digitizedCardId"),
    @SerializedName("illegal_param_driver_license")
    ILLEGAL_PARAM_DRIVER_LICENSE("illegal_param_driver_license"),
    @SerializedName("illegal_param_embossing_company")
    ILLEGAL_PARAM_EMBOSSING_COMPANY("illegal_param_embossing_company"),
    @SerializedName("illegal_param_embossing_type")
    ILLEGAL_PARAM_EMBOSSING_TYPE("illegal_param_embossing_type"),
    @SerializedName("illegal_param_expire_period")
    ILLEGAL_PARAM_EXPIRE_PERIOD("illegal_param_expire_period"),
    @SerializedName("illegal_param_expiry")
    ILLEGAL_PARAM_EXPIRY("illegal_param_expiry"),
    @SerializedName("illegal_param_ext_auth_fail_uri")
    ILLEGAL_PARAM_EXT_AUTH_FAIL_URI("illegal_param_ext_auth_fail_uri"),
    @SerializedName("illegal_param_ext_auth_success_uri")
    ILLEGAL_PARAM_EXT_AUTH_SUCCESS_URI("illegal_param_ext_auth_success_uri"),
    @SerializedName("illegal_param_external_reference")
    ILLEGAL_PARAM_EXTERNAL_REFERENCE("illegal_param_external_reference"),
    @SerializedName("illegal_param_favourite_id")
    ILLEGAL_PARAM_FAVORITE_ID("illegal_param_favourite_id"),
    @SerializedName("illegal_param_from")
    ILLEGAL_PARAM_FROM("illegal_param_from"),
    @SerializedName("illegal_param_id")
    ILLEGAL_PARAM_ID("illegal_param_id"),
    @SerializedName("illegal_param_instance_id")
    ILLEGAL_PARAM_INSTANCE_ID("illegal_param_instance_id"),
    @SerializedName("illegal_param_label")
    ILLEGAL_PARAM_LABEL("illegal_param_label"),
    @SerializedName("illegal_param_latitude")
    ILLEGAL_PARAM_LATITUDE("illegal_param_latitude"),
    @SerializedName("illegal_param_longitude")
    ILLEGAL_PARAM_LONGITUDE("illegal_param_longitude"),
    @SerializedName("illegal_param_message")
    ILLEGAL_PARAM_MESSAGE("illegal_param_message"),
    @SerializedName("illegal_param_mobilePin")
    ILLEGAL_PARAM_MOBILE_PIN("illegal_param_mobilePin"),
    @SerializedName("illegal_param_money_source_token")
    ILLEGAL_PARAM_MONEY_SOURCE_TOKEN("illegal_param_money_source_token"),
    @SerializedName("illegal_param_notification_client_type")
    ILLEGAL_PARAM_NOTIFICATION_CLIENT_TYPE("illegal_param_notification_client_type"),
    @SerializedName("illegal_param_notification_token")
    ILLEGAL_PARAM_NOTIFICATION_TOKEN("illegal_param_notification_token"),
    @SerializedName("illegal_param_oauth_token")
    ILLEGAL_PARAM_OAUTH_TOKEN("illegal_param_oauth_token"),
    @SerializedName("illegal_param_operation_id")
    ILLEGAL_PARAM_OPERATION_ID("illegal_param_operation_id"),
    @SerializedName("illegal_param_pan_fragment")
    ILLEGAL_PARAM_PAN_FRAGMENT("illegal_param_pan_fragment"),
    @SerializedName("illegal_param_phone_number")
    ILLEGAL_PARAM_PHONE_NUMBER("illegal_param_phone_number"),
    @SerializedName("illegal_param_protection_code")
    ILLEGAL_PARAM_PROTECTION_CODE("illegal_param_protection_code"),
    @SerializedName("illegal_param_query")
    ILLEGAL_PARAM_QUERY("illegal_param_query"),
    @SerializedName("illegal_param_records")
    ILLEGAL_PARAM_RECORDS("illegal_param_records"),
    @SerializedName("illegal_param_request_id")
    ILLEGAL_PARAM_REQUEST_ID("illegal_param_request_id"),
    @SerializedName("illegal_param_start_record")
    ILLEGAL_PARAM_START_RECORD("illegal_param_start_record"),
    @SerializedName("illegal_param_till")
    ILLEGAL_PARAM_TILL("illegal_param_till"),
    @SerializedName("illegal_param_title")
    ILLEGAL_PARAM_TITLE("illegal_param_title"),
    @SerializedName("illegal_param_to")
    ILLEGAL_PARAM_TO("illegal_param_to"),
    @SerializedName("illegal_param_type")
    ILLEGAL_PARAM_TYPE("illegal_param_type"),
    @SerializedName("illegal_param_uuid")
    ILLEGAL_PARAM_UUID("illegal_param_uuid"),
    @SerializedName("illegal_param_vehicle_reg_certificate")
    ILLEGAL_PARAM_VEHICLE_REG_CERTIFICATE("illegal_param_vehicle_reg_certificate"),
    @SerializedName("illegal_params")
    ILLEGAL_PARAMS("illegal_params"),
    @SerializedName("illegal_request_id")
    ILLEGAL_REQUEST_ID("illegal_request_id"),
    @SerializedName("invalid_grant")
    INVALID_GRANT("invalid_grant"),
    @SerializedName("invalid_image")
    INVALID_IMAGE("invalid_image"),
    @SerializedName("invalid_request")
    INVALID_REQUEST("invalid_request"),
    @SerializedName("invalid_scope")
    INVALID_SCOPE("invalid_scope"),
    @SerializedName("not_enough_funds")
    NOT_ENOUGH_FUNDS("not_enough_funds"),
    @SerializedName("limit_exceeded")
    LIMIT_EXCEEDED("limit_exceeded"),
    @SerializedName("linked_phone_required")
    LINKED_PHONE_REQUIRED("linked_phone_required"),
    @SerializedName("money_source_not_available")
    MONEY_SOURCE_NOT_AVAILABLE("money_source_not_available"),
    @SerializedName("payee_not_found")
    PAYEE_NOT_FOUND("payee_not_found"),
    @SerializedName("payment_expired")
    PAYMENT_EXPIRED("payment_expired"),
    @SerializedName("payment_refused")
    PAYMENT_REFUSED("payment_refused"),
    @SerializedName("phone_number_refused")
    PHONE_NUMBER_REFUSED("phone_number_refused"),
    @SerializedName("PIN_CHANGE_TIME_NOT_YET")
    PIN_CHANGE_TIME_NOT_YET("PIN_CHANGE_TIME_NOT_YET"),
    @SerializedName("pin_set_refused")
    PIN_SET_REFUSED("pin_set_refused"),
    @SerializedName("subscription_refused")
    SUBSCRIPTION_REFUSED("subscription_refused"),
    @SerializedName("technical_error")
    TECHNICAL_ERROR("technical_error"),
    @SerializedName("too_many_records")
    TOO_MANY_RECORDS("too_many_records"),
    @SerializedName("2fa_required")
    TWO_FA_REQUIRED("2fa_required"),
    @SerializedName("unauthorized_client")
    UNAUTHORIZED_CLIENT("unauthorized_client"),
    @SerializedName("unknown")
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
