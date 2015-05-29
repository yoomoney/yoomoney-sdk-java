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
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.utils.Strings;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Process external payment.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class ProcessExternalPayment extends BaseProcessPayment {

    public final ExternalCard externalCard;

    /**
     * Constructor.
     *
     * @param externalCard money source info if asked for a money source token
     *
     * @see com.yandex.money.api.methods.BaseProcessPayment
     */
    public ProcessExternalPayment(Status status, Error error, String invoiceId, String acsUri,
                                  Map<String, String> acsParams, Long nextRetry,
                                  ExternalCard externalCard) {
        super(status, error, invoiceId, acsUri, acsParams, nextRetry);
        this.externalCard = externalCard;
    }

    @Override
    public String toString() {
        return super.toString() + "ProcessExternalPayment{" +
                "externalCard=" + externalCard +
                '}';
    }

    /**
     * Request for processing external payment.
     */
    public static final class Request extends PostRequest<ProcessExternalPayment> {

        /**
         * For paying with a new card.
         *
         * @param instanceId application's instance id
         * @param requestId request id from {@link com.yandex.money.api.methods.RequestExternalPayment}
         * @param extAuthSuccessUri success URI to use if payment succeeded
         * @param extAuthFailUri fail URI to use if payment failed
         * @param requestToken {@code true} if money source token is required
         */
        public Request(String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, boolean requestToken) {
            this(instanceId, requestId, extAuthSuccessUri, extAuthFailUri, requestToken, null, null);
        }

        /**
         * For paying with a saved card.
         *
         * @param instanceId application's instance id
         * @param requestId request id from {@link com.yandex.money.api.methods.RequestExternalPayment}
         * @param extAuthSuccessUri success URI to use if payment succeeded
         * @param extAuthFailUri fail URI to use if payment failed
         * @param externalCard money source token of a saved card
         * @param csc Card Security Code for a saved card.
         */
        public Request(String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, ExternalCard externalCard, String csc) {
            this(instanceId, requestId, extAuthSuccessUri, extAuthFailUri, false, externalCard,
                    csc);
        }

        private Request(String instanceId, String requestId, String extAuthSuccessUri,
                        String extAuthFailUri, boolean requestToken, ExternalCard externalCard,
                        String csc) {

            super(ProcessExternalPayment.class, new Deserializer());
            if (Strings.isNullOrEmpty(instanceId)) {
                throw new IllegalArgumentException("instanceId is null or empty");
            }
            if (Strings.isNullOrEmpty(requestId)) {
                throw new IllegalArgumentException("requestId is null or empty");
            }
            if (Strings.isNullOrEmpty(extAuthSuccessUri)) {
                throw new IllegalArgumentException("extAuthSuccessUri is null or empty");
            }
            if (Strings.isNullOrEmpty(extAuthFailUri)) {
                throw new IllegalArgumentException("extAuthFailUri is null or empty");
            }

            addParameter("instance_id", instanceId);
            addParameter("request_id", requestId);
            addParameter("ext_auth_success_uri", extAuthSuccessUri);
            addParameter("ext_auth_fail_uri", extAuthFailUri);
            addParameter("request_token", requestToken);
            if (externalCard != null) {
                addParameter("money_source_token", externalCard.moneySourceToken);
                addParameter("csc", csc);
            }
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/process-external-payment";
        }

        private static final class Deserializer
                implements JsonDeserializer<ProcessExternalPayment> {

            @Override
            public ProcessExternalPayment deserialize(JsonElement json, Type typeOfT,
                                                      JsonDeserializationContext context)
                    throws JsonParseException {

                JsonObject o = json.getAsJsonObject();

                JsonObject paramsObj = o.getAsJsonObject(MEMBER_ACS_PARAMS);
                Map<String, String> acsParams = paramsObj == null ?
                        new HashMap<String, String>() : JsonUtils.map(paramsObj);

                final String moneySourceMember = "money_source";
                ExternalCard moneySource = o.has(moneySourceMember) ?
                        ExternalCard.createFromJson(o.get(moneySourceMember)) : null;

                return new ProcessExternalPayment(
                        Status.parse(JsonUtils.getString(o, MEMBER_STATUS)),
                        Error.parse(JsonUtils.getString(o, MEMBER_ERROR)),
                        JsonUtils.getString(o, "invoice_id"),
                        JsonUtils.getString(o, MEMBER_ACS_URI),
                        acsParams,
                        JsonUtils.getLong(o, MEMBER_NEXT_RETRY),
                        moneySource
                );
            }
        }
    }
}
