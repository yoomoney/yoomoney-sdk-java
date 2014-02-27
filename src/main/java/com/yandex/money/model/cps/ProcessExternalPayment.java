package com.yandex.money.model.cps;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.Utils;
import com.yandex.money.model.cps.misc.MoneySource;
import com.yandex.money.net.IRequest;
import com.yandex.money.net.PostRequestBodyBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 */
public class ProcessExternalPayment {

    private String status;
    private String error;
    private String acsUri;
    private Map<String, String> acsParams;
    private MoneySource moneySource;
    private Long nextRetry;
    private String invoiceId;

    public ProcessExternalPayment(String status, String error, String acsUri, Map<String, String> acsParams,
                                  MoneySource moneySource, Long nextRetry, String invoiceId) {
        this.status = status;
        this.error = error;
        this.acsUri = acsUri;
        this.acsParams = acsParams;
        this.moneySource = moneySource;
        this.nextRetry = nextRetry;
        this.invoiceId = invoiceId;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getAcsUri() {
        return acsUri;
    }

    public Map<String, String> getAcsParams() {
        return acsParams;
    }

    public MoneySource getMoneySource() {
        return moneySource;
    }

    public Long getNextRetry() {
        return nextRetry;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public boolean isSuccess() {
        return Status.SUCCESS.equals(status);
    }

    public boolean isInProgress() {
        return Status.IN_PROGRESS.equals(status);
    }

    public boolean isExtAuthRequired() {
        return Status.EXT_AUTH_REQUIRED.equals(status);
    }

    public static class Request implements IRequest<ProcessExternalPayment> {

        private String accessToken;
        private String instanceId;
        private String requestId;
        private String extAuthSuccessUri;
        private String extAuthFailUri;
        private boolean requestToken;
        private String moneySourceToken;
        private String csc;

        public Request(String accessToken, String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, boolean requestToken) {
            setupCommonParams(accessToken, instanceId, requestId, extAuthSuccessUri, extAuthFailUri);
            this.requestToken = requestToken;
        }

        public Request(String accessToken, String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, String moneySourceToken, String csc) {
            setupCommonParams(accessToken, instanceId, requestId, extAuthSuccessUri, extAuthFailUri);
            this.moneySourceToken = moneySourceToken;
            this.csc = csc;
        }

        private void setupCommonParams(String accessToken, String instanceId, String requestId, String extAuthSuccessUri, String extAuthFailUri) {
            this.accessToken = accessToken;

            if (Utils.isEmpty(instanceId))
                throw new IllegalArgumentException(Utils.emptyParam("instanceId"));
            this.instanceId = instanceId;

            if (Utils.isEmpty(requestId))
                throw new IllegalArgumentException(Utils.emptyParam("requestId"));
            this.requestId = requestId;

            if (Utils.isEmpty(extAuthSuccessUri))
                throw new IllegalArgumentException(Utils.emptyParam("extAuthSuccessUri"));
            this.extAuthSuccessUri = extAuthSuccessUri;

            if (Utils.isEmpty(extAuthFailUri))
                throw new IllegalArgumentException(Utils.emptyParam("extAuthFailUri"));
            this.extAuthFailUri = extAuthFailUri;
        }

        @Override
        public URL requestURL() throws MalformedURLException {
            return new URL(URI_API + "/process-external-payment");
        }

        @Override
        public ProcessExternalPayment parseResponse(InputStream inputStream) {
            return new GsonBuilder().registerTypeAdapter(ProcessExternalPayment.class, new JsonDeserializer<ProcessExternalPayment>() {
                @Override
                public ProcessExternalPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject o = json.getAsJsonObject();

                    JsonObject paramsObj = o.getAsJsonObject("acs_params");
                    Map<String, String> acsParams = Utils.parse(paramsObj);

                    JsonObject objMoneySource = o.getAsJsonObject("money_source");
                    MoneySource moneySource = MoneySource.parseJson(objMoneySource);

                    return new ProcessExternalPayment(
                            Utils.getString(o, "status"),
                            Utils.getString(o, "error"),
                            Utils.getString(o, "acs_uri"),
                            acsParams,
                            moneySource,
                            Utils.getLong(o, "next_retry"),
                            Utils.getString(o, "invoice_id")
                    );
                }
            }).create().fromJson(new InputStreamReader(inputStream), ProcessExternalPayment.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            PostRequestBodyBuffer bb = new PostRequestBodyBuffer();

            bb.addParam("instance_id", instanceId);
            bb.addParam("request_id", requestId);
            bb.addParam("ext_auth_success_uri", extAuthSuccessUri);
            bb.addParam("ext_auth_fail_uri", extAuthFailUri);

            bb.addBooleanIfTrue("request_token", requestToken);
            if (!Utils.isEmpty(moneySourceToken)) {
                bb.addParam("money_source_token", moneySourceToken);
            }
            if (!Utils.isEmpty(csc)) {
                bb.addParam("csc", csc);
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
