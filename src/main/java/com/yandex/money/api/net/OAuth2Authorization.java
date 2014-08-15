package com.yandex.money.api.net;

import com.yandex.money.api.model.Scope;

import java.util.HashSet;
import java.util.Iterator;
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
                .setClientId(client.getId());
    }

    /**
     * Authorize POST parameters.
     */
    public static final class Params {
        private String clientId;
        private String responseType;
        private String redirectUri;
        private Set<Scope> scopes;

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
         * @param scope add required scope
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
         * Build provided parameters.
         *
         * @return parameters
         */
        public byte[] build() {
            PostRequestBodyBuffer buffer = new PostRequestBodyBuffer();
            if (scopes != null) {
                Iterator<Scope> iterator = scopes.iterator();
                StringBuilder builder = new StringBuilder(iterator.next().getCode());
                while (iterator.hasNext()) {
                    builder.append(" ").append(iterator.next());
                }
                buffer.addParam("scope", builder.toString());
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
