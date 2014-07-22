package com.yandex.money.model.cps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.net.HostsProvider;
import com.yandex.money.net.MethodRequest;
import com.yandex.money.net.MethodResponse;
import com.yandex.money.net.PostRequestBodyBuffer;
import com.yandex.money.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Token implements MethodResponse {

    private final String accessToken;
    private final Error error;

    public Token(String accessToken, Error error) {
        this.accessToken = accessToken;
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Error getError() {
        return error;
    }

    public static final class Request implements MethodRequest<Token> {

        private final String code;
        private final String clientId;
        private final String grantType;
        private final String redirectUri;
        private final String clientSecret;

        public Request(String code, String clientId, String redirectUri) {
            this(code, clientId, redirectUri, null);
        }

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
