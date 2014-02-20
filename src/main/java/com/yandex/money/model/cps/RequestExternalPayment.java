package com.yandex.money.model.cps;

import com.google.gson.*;
import com.yandex.money.model.common.params.ParamsP2P;
import com.yandex.money.model.common.params.ParamsPhone;
import com.yandex.money.Utils;
import com.yandex.money.net.IRequest;
import com.yandex.money.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 */
public class RequestExternalPayment {

    private String status;
    private String error;
    private String requestId;
    private BigDecimal contractAmount;
    private String title;

    public RequestExternalPayment(String status, String error, String requestId, String contractAmount, String title) {
        this.status = status;
        this.error = error;
        this.requestId = requestId;
        if (contractAmount != null)
            this.contractAmount = new BigDecimal(contractAmount);
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getRequestId() {
        return requestId;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSuccess() {
        return Utils.STATUS_SUCCESS.equals(status);
    }

    public static class Request implements IRequest<RequestExternalPayment> {

        private String accessToken;
        private String instanceId;
        private String patternId;
        private Map<String, String> params;

        private Request(String accessToken, String instanceId, String patternId, Map<String, String> params) {
            this.accessToken = accessToken;
            if (Utils.isEmpty(instanceId))
                throw new IllegalArgumentException(Utils.emptyParam("instanceId"));
            this.instanceId = instanceId;
            if (Utils.isEmpty(patternId))
                throw new IllegalArgumentException(Utils.emptyParam("patternId"));
            this.patternId = patternId;

            this.params = params;
        }

        public static Request newInstance(String accessToken, String instanceId, String patternId,
                                          Map<String, String> paramsShop) {
            if (paramsShop == null)
                throw new IllegalArgumentException(Utils.emptyParam("paramsShop"));

            return new Request(accessToken, instanceId, patternId, paramsShop);
        }

        public static Request newInstance(String accessToken, String instanceId, ParamsP2P paramsP2P) {
            if (paramsP2P == null)
                throw new IllegalArgumentException(Utils.emptyParam("paramsP2P"));

            return new Request(accessToken, instanceId, ParamsP2P.PATTERN_ID, paramsP2P.makeParams());
        }

        public static Request newInstance(String accessToken, String instanceId, ParamsPhone paramsPhone) {
            if (paramsPhone == null)
                throw new IllegalArgumentException(Utils.emptyParam("paramsPhone"));

            return new Request(accessToken, instanceId, ParamsPhone.PATTERN_ID, paramsPhone.makeParams());
        }

        @Override
        public URL requestURL() throws MalformedURLException {
            return new URL(URI_API + "/request-external-payment");
        }

        @Override
        public RequestExternalPayment parseResponse(InputStream inputStream) {
            return new GsonBuilder().registerTypeAdapter(RequestExternalPayment.class, new JsonDeserializer<RequestExternalPayment>() {
                @Override
                public RequestExternalPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject o = json.getAsJsonObject();
                    return new RequestExternalPayment(
                            Utils.getString(o, "status"),
                            Utils.getString(o, "error"),
                            Utils.getString(o, "request_id"),
                            Utils.getString(o, "contract_amount"),
                            Utils.getString(o, "title")
                            );
                }
            }).create().fromJson(new InputStreamReader(inputStream), RequestExternalPayment.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            PostRequestBodyBuffer bb = new PostRequestBodyBuffer();

            bb.addParam("instance_id", instanceId);
            bb.addParam("pattern_id", patternId);
            for (Map.Entry<String,String> entry : params.entrySet()) {
                bb.addParam(entry.getKey(), entry.getValue());
            }

            return bb;
        }

        @Override
        public void writeHeaders(HttpURLConnection connection) {
            if (accessToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
        }
    }
}
