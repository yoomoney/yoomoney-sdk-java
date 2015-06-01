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

package com.yandex.money.api.net;

import com.yandex.money.api.model.Scope;
import com.yandex.money.api.utils.Strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Builds parameters for OAuth2 authorization using application's web browser.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2Authorization {

    private final ApiClient client;

    /**
     * Constructor.
     *
     * @param client API client
     * @see com.yandex.money.api.net.ApiClient
     */
    public OAuth2Authorization(ApiClient client) {
        if (client == null) {
            throw new NullPointerException("hostsProvider is null");
        }
        this.client = client;
    }

    /**
     * URL to open in web browser.
     *
     * @return URL
     */
    public String getAuthorizeUrl() {
        return client.getHostsProvider().getSpMoney() + "/oauth/authorize";
    }

    /**
     * POST parameters for URL. Client ID is set by default.
     *
     * @return authorize parameters
     */
    public Params getAuthorizeParams() {
        return new Params()
                .setClientId(client.getClientId());
    }

    /**
     * Authorize POST parameters.
     */
    public static final class Params {
        private String clientId;
        private String responseType;
        private String redirectUri;
        private Set<Scope> scopes;
        private String rawScope;
        private String instanceName;

        /**
         * @param responseType specific response type
         */
        public Params setResponseType(String responseType) {
            this.responseType = responseType;
            return this;
        }

        /**
         * @param redirectUri redirect URI
         */
        public Params setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        /**
         * Scopes are added to a set so there are no two identical scopes in params.
         *
         * @param scope required scope
         */
        public Params addScope(Scope scope) {
            if (scope != null) {
                if (scopes == null) {
                    scopes = new HashSet<>();
                }
                scopes.add(scope);
            }
            return this;
        }

        /**
         * Sets raw scopes parameter. Overrides {@link #addScope(com.yandex.money.api.model.Scope)}.
         *
         * @param rawScope {@code scope} parameter
         */
        public Params setRawScope(String rawScope) {
            this.rawScope = rawScope;
            return this;
        }

        /**
         * Sets unique instance name for application.
         *
         * @param instanceName the instance name
         */
        public Params setInstanceName(String instanceName) {
            this.instanceName = instanceName;
            return this;
        }

        /**
         * Build provided parameters.
         *
         * @return parameters
         */
        public byte[] build() {
            Map<String, String> params = new HashMap<>();
            final String scopeName = "scope";
            if (Strings.isNullOrEmpty(rawScope)) {
                if (scopes != null) {
                    params.put(scopeName, Scope.createScopeParameter(scopes.iterator()));
                }
            } else {
                params.put(scopeName, rawScope);
            }
            params.put("client_id", clientId);
            params.put("response_type", responseType);
            params.put("redirect_uri", redirectUri);
            params.put("instance_name", instanceName);

            return new ParametersBuffer()
                    .setParams(params)
                    .prepareBytes();
        }

        private Params setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
    }
}
