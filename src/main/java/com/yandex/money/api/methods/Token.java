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
import com.yandex.money.api.utils.Strings;

import java.lang.reflect.Type;

/**
 * Access token.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Token implements MethodResponse {

    public final String accessToken;
    public final Error error;

    /**
     * Constructor.
     *
     * @param accessToken access token itself
     * @param error error code
     */
    public Token(String accessToken, Error error) {
        this.accessToken = accessToken;
        this.error = error;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", error=" + error +
                '}';
    }

    /**
     * Request for access token.
     */
    public static class Request extends PostRequest<Token> {

        /**
         * Constructor.
         *
         * @param code temporary code
         * @param clientId application's client id
         * @param redirectUri redirect uri
         */
        public Request(String code, String clientId, String redirectUri) {
            this(code, clientId, redirectUri, null);
        }

        /**
         * Constructor.
         *
         * @param code temporary code
         * @param clientId application's client id
         * @param redirectUri redirect uri
         * @param clientSecret a secret word for verifying application's authenticity.
         */
        public Request(String code, String clientId, String redirectUri, String clientSecret) {
            super(Token.class, new Deserializer());
            if (Strings.isNullOrEmpty(code)) {
                throw new NullPointerException("code is null or empty");
            }
            if (Strings.isNullOrEmpty(clientId)) {
                throw new NullPointerException("clientId is null or empty");
            }
            addParameter("code", code);
            addParameter("client_id", clientId);
            addParameter("grant_type", "authorization_code");
            addParameter("redirect_uri", redirectUri);
            addParameter("client_secret", clientSecret);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoney() + "/oauth/token";
        }
    }

    /**
     * Revokes access token.
     */
    public static final class Revoke extends PostRequest<Object> {

        private static final JsonDeserializer<Object> STUB_DESERIALIZER =
                new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context)
                            throws JsonParseException {
                        return null;
                    }
                };

        /**
         * Revoke only one token.
         */
        public Revoke() {
            this(false);
        }

        /**
         * Revoke token.
         *
         * @param revokeAll if {@code true} all bound tokens will be also revoked
         */
        public Revoke(boolean revokeAll) {
            super(Object.class, STUB_DESERIALIZER);
            addParameter("revoke-all", revokeAll);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/revoke";
        }
    }

    private static final class Deserializer implements JsonDeserializer<Token> {

        @Override
        public Token deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new Token(JsonUtils.getString(object, "access_token"),
                    Error.parse(JsonUtils.getString(object, "error")));
        }
    }
}
