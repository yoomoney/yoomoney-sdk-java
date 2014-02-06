package com.yandex.money.model;

import com.google.gson.*;
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
    private String error;
    private String instanceId;

    public InstanceId(String status, String error, String instanceId) {
        this.status = status;
        this.error = error;
        this.instanceId = instanceId;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getInstanceId() {
        return instanceId;
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
                            Utils.getString(o, "status"),
                            Utils.getString(o, "error"),
                            Utils.getString(o, "instance_id"));
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
