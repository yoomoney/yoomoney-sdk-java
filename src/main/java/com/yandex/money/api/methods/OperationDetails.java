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
import com.yandex.money.api.model.Operation;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;

import java.lang.reflect.Type;

/**
 * Operation details result.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class OperationDetails implements MethodResponse {

    public final Error error;
    public final Operation operation;

    /**
     * Constructor.
     *
     * @param error error code
     * @param operation operation
     */
    public OperationDetails(Error error, Operation operation) {
        this.error = error;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "OperationDetails{" +
                "error=" + error +
                ", operation=" + operation +
                '}';
    }

    /**
     * Requests for specific operation details.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static class Request extends PostRequest<OperationDetails> {

        /**
         * Constructor.
         *
         * @param operationId operation's id
         */
        public Request(String operationId) {
            super(OperationDetails.class, new Deserializer());
            addParameter("operation_id", operationId);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/operation-details";
        }
    }

    private static final class Deserializer implements JsonDeserializer<OperationDetails> {
        @Override
        public OperationDetails deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new OperationDetails(Error.parse(JsonUtils.getString(object, "error")),
                    Operation.createFromJson(json));
        }
    }
}
