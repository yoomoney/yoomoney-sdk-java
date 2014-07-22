package com.yandex.money.net;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2Authorization {

    private final HostsProvider hostsProvider;

    public OAuth2Authorization(HostsProvider hostsProvider) {
        if (hostsProvider == null) {
            throw new NullPointerException("hostsProvider is null");
        }
        this.hostsProvider = hostsProvider;
    }

    public String getAuthorizeUrl() {
        return hostsProvider.getSpMoney() + "/oauth/authorize";
    }

    public Params getAuthorizeParams() {
        return new Params();
    }

    public class Params {
        private String clientId;
        private String responseType;
        private String redirectUri;
        private Set<Scope> scopes;

        public Params setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Params setResponseType(String responseType) {
            this.responseType = responseType;
            return this;
        }

        public Params setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Params addScope(Scope scope) {
            if (scope != null) {
                if (scopes == null) {
                    scopes = new HashSet<>();
                }
                scopes.add(scope);
            }
            return this;
        }

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
    }
}
