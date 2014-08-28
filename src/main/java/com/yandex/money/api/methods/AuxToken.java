package com.yandex.money.api.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * Auxiliary token. Has scopes which are subset of {@link com.yandex.money.api.methods.Token}'s
 * scopes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AuxToken implements MethodResponse {

    private final String auxToken;
    private final Error error;

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
     * @return auxiliary token
     */
    public String getAuxToken() {
        return auxToken;
    }

    /**
     * @return error code
     */
    public Error getError() {
        return error;
    }

    /**
     * Requests for an auxiliary token.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request implements MethodRequest<AuxToken> {

        private final Set<Scope> scopes;

        public Request(Set<Scope> scopes) {
            if (scopes == null || scopes.isEmpty()) {
                throw new IllegalArgumentException("scopes is null or empty");
            }
            this.scopes = scopes;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getSpMoney() + "/oauth/token-aux");
        }

        @Override
        public AuxToken parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), AuxToken.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addParam("scope", Scope.createScopeParameter(scopes.iterator()));
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(AuxToken.class, new Deserializer())
                    .create();
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
