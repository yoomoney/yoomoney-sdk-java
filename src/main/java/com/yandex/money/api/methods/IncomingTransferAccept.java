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

package com.yandex.money.api.methods;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;

import java.lang.reflect.Type;

/**
 * Incoming transfer accept result.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferAccept implements MethodResponse {

    public final Status status;
    public final Error error;
    public final Integer protectionCodeAttemptsAvailable;
    public final String extActionUri;

    /**
     * Constructor.
     *
     * @param status status of an operation
     * @param error error code
     * @param protectionCodeAttemptsAvailable number of attempts available after invalid protection
     *                                        code submission
     * @param extActionUri address to perform external action for successful acceptance
     */
    public IncomingTransferAccept(Status status, Error error,
                                  Integer protectionCodeAttemptsAvailable, String extActionUri) {
        this.status = status;
        this.error = error;
        this.protectionCodeAttemptsAvailable = protectionCodeAttemptsAvailable;
        this.extActionUri = extActionUri;
    }

    @Override
    public String toString() {
        return "IncomingTransferAccept{" +
                "status=" + status +
                ", error=" + error +
                ", protectionCodeAttemptsAvailable=" + protectionCodeAttemptsAvailable +
                ", extActionUri='" + extActionUri + '\'' +
                '}';
    }

    /**
     * Status of {@link com.yandex.money.api.methods.IncomingTransferAccept}
     */
    public enum Status {
        /**
         * Successful.
         */
        SUCCESS(CODE_SUCCESS),
        /**
         * Operation refused.
         */
        REFUSED(CODE_REFUSED),
        /**
         * Unknown status.
         */
        UNKNOWN(CODE_UNKNOWN);

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.code.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Requests to perform {@link com.yandex.money.api.methods.IncomingTransferAccept}.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<IncomingTransferAccept> {

        /**
         * Constructor.
         *
         * @param operationId unique operation id
         * @param protectionCode protection code if transfer is protected
         */
        public Request(String operationId, String protectionCode) {
            super(IncomingTransferAccept.class, new Deserializer());
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            addParameter("operation_id", operationId);
            addParameter("protection_code", protectionCode);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/incoming-transfer-accept";
        }
    }

    private static final class Deserializer implements JsonDeserializer<IncomingTransferAccept> {
        @Override
        public IncomingTransferAccept deserialize(JsonElement json, Type typeOfT,
                                                  JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new IncomingTransferAccept(
                    Status.parse(JsonUtils.getMandatoryString(object, "status")),
                    Error.parse(JsonUtils.getString(object, "error")),
                    JsonUtils.getInt(object, "protection_code_attempts_available"),
                    JsonUtils.getString(object, "ext_action_uri"));
        }
    }
}
