/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NBCO Yandex.Money LLC
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

package com.yandex.money.api.authorization;

import com.yandex.money.api.model.Scope;
import com.yandex.money.api.util.Strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Authorization parameters for {@link AuthorizationData#getParameters()}.
 */
public interface AuthorizationParameters {

    /**
     * Adds parameter to the list.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    void add(String name, String value);

    /**
     * Builds parameters for usage in a request.
     *
     * @return byte array of the parameters
     */
    byte[] build();

    final class Builder {

        private String responseType;
        private String redirectUri;
        private Set<Scope> scopes;
        private String rawScope;
        private String instanceName;

        /**
         * @param responseType specific response type
         */
        public Builder setResponseType(String responseType) {
            this.responseType = responseType;
            return this;
        }

        /**
         * @param redirectUri redirect URI
         */
        public Builder setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        /**
         * Scopes are added to a set so there are no two identical scopes in params.
         *
         * @param scope required scope
         */
        public Builder addScope(Scope scope) {
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
        public Builder setRawScope(String rawScope) {
            this.rawScope = rawScope;
            return this;
        }

        /**
         * Sets unique instance name for application.
         *
         * @param instanceName the instance name
         */
        public Builder setInstanceName(String instanceName) {
            this.instanceName = instanceName;
            return this;
        }

        /**
         * Build provided parameters.
         *
         * @return parameters
         */
        public AuthorizationParameters build() {
            Map<String, String> params = new HashMap<>();
            final String scopeName = "scope";
            if (Strings.isNullOrEmpty(rawScope)) {
                if (scopes != null) {
                    params.put(scopeName, Scope.createScopeParameter(scopes));
                }
            } else {
                params.put(scopeName, rawScope);
            }
            params.put("response_type", responseType);
            params.put("redirect_uri", redirectUri);
            params.put("instance_name", instanceName);

            return new AuthorizationParametersImpl(params);
        }
    }
}
