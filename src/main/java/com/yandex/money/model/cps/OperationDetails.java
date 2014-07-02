package com.yandex.money.model.cps;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.misc.Operation;

import java.lang.reflect.Type;

public class OperationDetails {

    private final String error;
    private final Operation operation;

    public OperationDetails(String error, Operation operation) {
        this.error = error;
        this.operation = operation;
    }

    public String getError() {
        return error;
    }

    public Operation getOperation() {
        return operation;
    }

    private static final class Deserializer implements JsonDeserializer<OperationDetails> {
        @Override
        public OperationDetails deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new OperationDetails(JsonUtils.getString(object, "error"),
                    Operation.createFromJson(json));
        }
    }

    public static class Request {

        private final String operationId;

        public Request(String operationId) {
            this.operationId = operationId;
        }
    }
}
