package com.yandex.money.api.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequestBodyBuffer;
import com.yandex.money.api.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

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
    public static class Request implements MethodRequest<Token> {

        protected final String code;
        protected final String clientId;

        private final String grantType;
        private final String redirectUri;
        private final String clientSecret;

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
            if (Strings.isNullOrEmpty(code)) {
                throw new NullPointerException("code is null or empty");
            }
            this.code = code;
            if (Strings.isNullOrEmpty(clientId)) {
                throw new NullPointerException("clientId is null or empty");
            }
            this.clientId = clientId;
            this.grantType = "authorization_code";
            if (Strings.isNullOrEmpty(redirectUri)) {
                throw new NullPointerException("redirectUri is null or empty");
            }
            this.redirectUri = redirectUri;
            this.clientSecret = clientSecret;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getSpMoney() + "/oauth/token");
        }

        @Override
        public Token parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), Token.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addParam("code", code)
                    .addParam("client_id", clientId)
                    .addParam("grant_type", grantType)
                    .addParamIfNotNull("redirect_uri", redirectUri)
                    .addParamIfNotNull("client_secret", clientSecret);
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(Token.class, new Deserializer())
                    .create();
        }
    }

    /**
     * Revokes access token.
     */
    public static final class Revoke implements MethodRequest<Object> {

        private final boolean revokeAll;

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
            this.revokeAll = revokeAll;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/revoke");
        }

        @Override
        public Object parseResponse(InputStream inputStream) {
            return null;
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addBooleanIfTrue("revoke-all", revokeAll);
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
