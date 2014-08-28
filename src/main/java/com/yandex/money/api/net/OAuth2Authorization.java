package com.yandex.money.api.net;

import com.yandex.money.api.model.Scope;
import com.yandex.money.api.utils.Strings;

import java.util.HashSet;
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
         * Build provided parameters.
         *
         * @return parameters
         */
        public byte[] build() {
            PostRequestBodyBuffer buffer = new PostRequestBodyBuffer();
            final String scopeName = "scope";
            if (Strings.isNullOrEmpty(rawScope)) {
                if (scopes != null) {
                    buffer.addParam(scopeName, Scope.createScopeParameter(scopes.iterator()));
                }
            } else {
                buffer.addParam(scopeName, rawScope);
            }
            return buffer
                    .addParamIfNotNull("client_id", clientId)
                    .addParamIfNotNull("response_type", responseType)
                    .addParamIfNotNull("redirect_uri", redirectUri)
                    .toByteArray();
        }

        private Params setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
    }
}
