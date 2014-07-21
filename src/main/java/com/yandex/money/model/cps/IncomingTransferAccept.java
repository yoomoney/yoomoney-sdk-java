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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferAccept implements MethodResponse {

    private final Status status;
    private final Error error;
    private final Integer protectionCodeAttemptsAvailable;
    private final String extActionUri;

    public IncomingTransferAccept(Status status, Error error,
                                  Integer protectionCodeAttemptsAvailable, String extActionUri) {
        this.status = status;
        this.error = error;
        this.protectionCodeAttemptsAvailable = protectionCodeAttemptsAvailable;
        this.extActionUri = extActionUri;
    }

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public Integer getProtectionCodeAttemptsAvailable() {
        return protectionCodeAttemptsAvailable;
    }

    public String getExtActionUri() {
        return extActionUri;
    }

    public enum Status {
        SUCCESS(CODE_SUCCESS),
        REFUSED(CODE_REFUSED),
        UNKNOWN(CODE_UNKNOWN);

        private final String status;

        private Status(String status) {
            this.status = status;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.status.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    public static final class Request implements MethodRequest<IncomingTransferAccept> {

        private final String operationId;
        private final String protectionCode;

        public Request(String operationId, String protectionCode) {
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            this.operationId = operationId;
            this.protectionCode = protectionCode;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/incoming-transfer-accept");
        }

        @Override
        public IncomingTransferAccept parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream),
                    IncomingTransferAccept.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addParam("operation_id", operationId)
                    .addParamIfNotNull("protection_code", protectionCode);
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(IncomingTransferAccept.class, new Deserializer())
                    .create();
        }
    }

    private static final class Deserializer implements JsonDeserializer<IncomingTransferAccept> {
        @Override
        public IncomingTransferAccept deserialize(JsonElement json, Type typeOfT,
                                                  JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new IncomingTransferAccept(
                    Status.parse(JsonUtils.getMandatoryString(object, "status")),
                    Error.parse(JsonUtils.getString(object, "error")),
                    JsonUtils.getInt(object, "protection_code_attempts_available"),
                    JsonUtils.getString(object, "ext_action_uri"));
        }
    }
}
