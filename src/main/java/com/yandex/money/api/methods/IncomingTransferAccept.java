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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Incoming transfer accept result.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferAccept implements MethodResponse {

    public final Status status;
    public final Error error;
    public final Integer protectionCodeAttemptsAvailable;
    public final String extActionUri;

    /**
     * Constructor.
     *
     * @param status status of an operation
     * @param error error code
     * @param protectionCodeAttemptsAvailable number of attempts available after invalid protection
     *                                        code submission
     * @param extActionUri address to perform external action for successful acceptance
     */
    public IncomingTransferAccept(Status status, Error error,
                                  Integer protectionCodeAttemptsAvailable, String extActionUri) {
        this.status = status;
        this.error = error;
        this.protectionCodeAttemptsAvailable = protectionCodeAttemptsAvailable;
        this.extActionUri = extActionUri;
    }

    @Override
    public String toString() {
        return "IncomingTransferAccept{" +
                "status=" + status +
                ", error=" + error +
                ", protectionCodeAttemptsAvailable=" + protectionCodeAttemptsAvailable +
                ", extActionUri='" + extActionUri + '\'' +
                '}';
    }

    /**
     * Status of {@link com.yandex.money.api.methods.IncomingTransferAccept}
     */
    public enum Status {
        /**
         * Successful.
         */
        SUCCESS(CODE_SUCCESS),
        /**
         * Operation refused.
         */
        REFUSED(CODE_REFUSED),
        /**
         * Unknown status.
         */
        UNKNOWN(CODE_UNKNOWN);

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.code.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Requests to perform {@link com.yandex.money.api.methods.IncomingTransferAccept}.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request implements MethodRequest<IncomingTransferAccept> {

        private final String operationId;
        private final String protectionCode;

        /**
         * Constructor.
         *
         * @param operationId unique operation id
         * @param protectionCode protection code if transfer is protected
         */
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
