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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Incoming transfer reject operation.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferReject implements MethodResponse {

    public final Status status;
    public final Error error;

    /**
     * Constructor.
     *
     * @param status status of an operation
     * @param error error code
     */
    public IncomingTransferReject(Status status, Error error) {
        this.status = status;
        this.error = error;
    }

    @Override
    public String toString() {
        return "IncomingTransferReject{" +
                "status=" + status +
                ", error=" + error +
                '}';
    }

    /**
     * Status of rejection.
     */
    public enum Status {
        /**
         * Successful.
         */
        SUCCESS(CODE_SUCCESS),
        /**
         * Refused.
         */
        REFUSED(CODE_REFUSED),
        /**
         * Unknown.
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
     * Rejects incoming transfer.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request implements ApiRequest<IncomingTransferReject> {

        private String operationId;

        /**
         * Constructor.
         *
         * @param operationId rejecting operation id
         */
        public Request(String operationId) {
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            this.operationId = operationId;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/incoming-transfer-reject");
        }

        @Override
        public IncomingTransferReject parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream),
                    IncomingTransferReject.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addParam("operation_id", operationId);
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(Request.class, new Deserializer())
                    .create();
        }
    }

    private static final class Deserializer implements JsonDeserializer<IncomingTransferReject> {
        @Override
        public IncomingTransferReject deserialize(JsonElement json, Type typeOfT,
                                                  JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new IncomingTransferReject(
                    Status.parse(JsonUtils.getMandatoryString(object, "status")),
                    Error.parse(JsonUtils.getString(object, "error")));
        }
    }
}
