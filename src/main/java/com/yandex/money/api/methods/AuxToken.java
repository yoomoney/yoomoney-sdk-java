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
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Auxiliary token. Has scopes which are subset of {@link com.yandex.money.api.methods.Token}'s
 * scopes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AuxToken implements MethodResponse {

    /**
     * auxiliary token
     */
    public final String auxToken;

    /**
     * error code
     */
    public final Error error;

    public AuxToken(String auxToken, Error error) {
        this.auxToken = auxToken;
        this.error = error;
    }

    @Override
    public String toString() {
        return "AuxToken{" +
                "auxToken='" + auxToken + '\'' +
                ", error=" + error +
                '}';
    }

    /**
     * Requests for an auxiliary token.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<AuxToken> {

        public Request(Set<Scope> scopes) {
            super(AuxToken.class, new Deserializer());
            if (scopes == null || scopes.isEmpty()) {
                throw new IllegalArgumentException("scopes is null or empty");
            }
            addParameter("scope", Scope.createScopeParameter(scopes.iterator()));
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/token-aux";
        }
    }

    private static final class Deserializer implements JsonDeserializer<AuxToken> {

        @Override
        public AuxToken deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new AuxToken(JsonUtils.getString(object, "aux_token"),
                    Error.parse(JsonUtils.getString(object, "error")));
        }
    }
}
