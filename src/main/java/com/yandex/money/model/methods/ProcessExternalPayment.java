package com.yandex.money.model.methods;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.methods.misc.MoneySourceExternal;
import com.yandex.money.net.HostsProvider;
import com.yandex.money.net.MethodRequest;
import com.yandex.money.net.PostRequestBodyBuffer;
import com.yandex.money.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 */
public class ProcessExternalPayment extends BaseProcessPayment {

    private final MoneySourceExternal moneySource;
    private final String invoiceId;

    public ProcessExternalPayment(Status status, Error error, String acsUri,
                                  Map<String, String> acsParams, MoneySourceExternal moneySource,
                                  Long nextRetry, String invoiceId) {

        super(status, error, acsUri, acsParams, nextRetry);
        this.moneySource = moneySource;
        this.invoiceId = invoiceId;
    }

    public MoneySourceExternal getMoneySource() {
        return moneySource;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public static class Request implements MethodRequest<ProcessExternalPayment> {

        private final String instanceId;
        private final String requestId;
        private final String extAuthSuccessUri;
        private final String extAuthFailUri;
        private final boolean requestToken;
        private final String moneySourceToken;
        private final String csc;

        public Request(String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, boolean requestToken) {
            this(instanceId, requestId, extAuthSuccessUri, extAuthFailUri, requestToken, null, null);
        }

        public Request(String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, String moneySourceToken, String csc) {
            this(instanceId, requestId, extAuthSuccessUri, extAuthFailUri, false, moneySourceToken,
                    csc);
        }

        private Request(String instanceId, String requestId, String extAuthSuccessUri,
                        String extAuthFailUri, boolean requestToken, String moneySourceToken,
                        String csc) {

            if (Strings.isNullOrEmpty(instanceId))
                throw new IllegalArgumentException("instanceId is null or empty");
            this.instanceId = instanceId;

            if (Strings.isNullOrEmpty(requestId))
                throw new IllegalArgumentException("requestId is null or empty");
            this.requestId = requestId;

            if (Strings.isNullOrEmpty(extAuthSuccessUri))
                throw new IllegalArgumentException("extAuthSuccessUri is null or empty");
            this.extAuthSuccessUri = extAuthSuccessUri;

            if (Strings.isNullOrEmpty(extAuthFailUri))
                throw new IllegalArgumentException("extAuthFailUri is null or empty");
            this.extAuthFailUri = extAuthFailUri;

            if (requestToken) {
                this.requestToken = true;
                this.moneySourceToken = null;
                this.csc = null;
            } else {
                this.requestToken = false;
                this.moneySourceToken = moneySourceToken;
                this.csc = csc;
            }
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/process-external-payment");
        }

        @Override
        public ProcessExternalPayment parseResponse(InputStream inputStream) {
            return new GsonBuilder().registerTypeAdapter(ProcessExternalPayment.class, new JsonDeserializer<ProcessExternalPayment>() {
                @Override
                public ProcessExternalPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject o = json.getAsJsonObject();

                    JsonObject paramsObj = o.getAsJsonObject(MEMBER_ACS_PARAMS);
                    Map<String, String> acsParams = JsonUtils.map(paramsObj);

                    JsonObject objMoneySource = o.getAsJsonObject("money_source");
                    MoneySourceExternal moneySource = MoneySourceExternal.parseJson(objMoneySource);

                    return new ProcessExternalPayment(
                            Status.parse(JsonUtils.getString(o, MEMBER_STATUS)),
                            Error.parse(JsonUtils.getString(o, MEMBER_ERROR)),
                            JsonUtils.getString(o, MEMBER_ACS_URI),
                            acsParams,
                            moneySource,
                            JsonUtils.getLong(o, MEMBER_NEXT_RETRY),
                            JsonUtils.getString(o, "invoice_id")
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
            if (!Strings.isNullOrEmpty(moneySourceToken)) {
                bb.addParam("money_source_token", moneySourceToken);
            }
            if (!Strings.isNullOrEmpty(csc)) {
                bb.addParam("csc", csc);
            }

            return bb;
        }
    }
}
