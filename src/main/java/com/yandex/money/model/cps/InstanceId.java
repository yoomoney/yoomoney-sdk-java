package com.yandex.money.model.cps;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.Utils;
import com.yandex.money.net.IRequest;
import com.yandex.money.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class InstanceId {

    private String status;
    private Error error;
    private String instanceId;

    public InstanceId(String status, Error error, String instanceId) {
        this.status = status;
        this.error = error;
        this.instanceId = instanceId;
    }

    public String getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public boolean isSuccess() {
        return Status.SUCCESS.equals(status);
    }

    public static class Request implements IRequest<InstanceId> {

        private String clientId;

        public Request(String clientId) {
            if (Utils.isEmpty(clientId))
                throw new IllegalArgumentException(Utils.emptyParam("clientId"));
            this.clientId = clientId;
        }

        @Override
        public URL requestURL() throws MalformedURLException {
            return new URL(URI_API + "/instance-id");
        }

        @Override
        public InstanceId parseResponse(InputStream inputStream) {
            return new GsonBuilder().registerTypeAdapter(InstanceId.class, new JsonDeserializer<InstanceId>() {
                @Override
                public InstanceId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject o = json.getAsJsonObject();
                    return new InstanceId(
                            JsonUtils.getString(o, "status"),
                            Error.parse(JsonUtils.getString(o, "error")),
                            JsonUtils.getString(o, "instance_id"));
                }
            }).create().fromJson(new InputStreamReader(inputStream), InstanceId.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer().addParam("client_id", clientId);
        }

        @Override
        public void writeHeaders(HttpURLConnection connection) {
        }

    }
}
