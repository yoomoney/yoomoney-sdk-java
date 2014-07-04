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
public class IncomingTransferAccept {

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

        private final String operationId;
        private final String protectionCode;

        public Request(String operationId, String protectionCode) {
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            this.operationId = operationId;
            this.protectionCode = protectionCode;
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
