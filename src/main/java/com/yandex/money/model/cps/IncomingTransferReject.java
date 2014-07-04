package com.yandex.money.model.cps;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class IncomingTransferReject {

    private final Status status;
    private final Error error;

    public IncomingTransferReject(Status status, Error error) {
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public enum Status {
        SUCCESS("success"),
        REFUSED("refused"),
        UNKNOWN("unknown");

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

    public static final class Request {
        private String operationId;

        public Request(String operationId) {
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            this.operationId = operationId;
        }
    }

    private static final class Deserializer implements JsonDeserializer<IncomingTransferReject> {
        @Override
        public IncomingTransferReject deserialize(JsonElement json, Type typeOfT,
                                                  JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new IncomingTransferReject(
                    Status.parse(JsonUtils.getMandatoryString(object, "status")),
                    Error.parse(JsonUtils.getString(object, "error")));
        }
    }
}
