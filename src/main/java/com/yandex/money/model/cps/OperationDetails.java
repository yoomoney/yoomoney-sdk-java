package com.yandex.money.model.cps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.misc.Operation;
import com.yandex.money.net.IRequest;
import com.yandex.money.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static class Request implements IRequest<OperationDetails> {

        private final String operationId;

        public Request(String operationId) {
            if (operationId == null || operationId.isEmpty()) {
                throw new IllegalArgumentException("operationId is null or empty");
            }
            this.operationId = operationId;
        }

        @Override
        public URL requestURL() throws MalformedURLException {
            return new URL(URI_API + "operation-details");
        }

        @Override
        public OperationDetails parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), OperationDetails.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addParam("operation_id", operationId);
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(OperationDetails.class, new Deserializer())
                    .create();
        }
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
}
