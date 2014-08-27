package com.yandex.money.api.methods;

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
 * Instance ID result.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class InstanceId implements MethodResponse {

    private Status status;
    private Error error;
    private String instanceId;

    /**
     * Constructor.
     *
     * @param status status of an operation
     * @param error error code
     * @param instanceId instance id if success
     */
    public InstanceId(Status status, Error error, String instanceId) {
        this.status = status;
        this.error = error;
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "InstanceId{" +
                "status=" + status +
                ", error=" + error +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    /**
     * Status of an instance id request.
     */
    public enum Status {
        /**
         * Successful.
         */
        SUCCESS(CODE_SUCCESS),
        /**
         * Refused due to various reasons.
         */
        REFUSED(CODE_REFUSED),
        /**
         * Unknown.
         */
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

    /**
     * Request for a new instance id.
     */
    public static class Request implements MethodRequest<InstanceId> {

        private String clientId;

        /**
         * Construct request using provided client ID.
         *
         * @param clientId client id of the application
         */
        public Request(String clientId) {
            if (Strings.isNullOrEmpty(clientId))
                throw new IllegalArgumentException("clientId is null or empty");
            this.clientId = clientId;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/instance-id");
        }

        @Override
        public InstanceId parseResponse(InputStream inputStream) {
            return new GsonBuilder().registerTypeAdapter(InstanceId.class, new JsonDeserializer<InstanceId>() {
                @Override
                public InstanceId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject o = json.getAsJsonObject();
                    return new InstanceId(
                            Status.parse(JsonUtils.getString(o, "status")),
                            Error.parse(JsonUtils.getString(o, "error")),
                            JsonUtils.getString(o, "instance_id"));
                }
            }).create().fromJson(new InputStreamReader(inputStream), InstanceId.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer().addParam("client_id", clientId);
        }
    }
}
