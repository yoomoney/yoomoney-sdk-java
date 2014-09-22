package com.yandex.money.api.methods;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.params.P2pParams;
import com.yandex.money.api.methods.params.PhoneParams;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.PostRequestBodyBuffer;
import com.yandex.money.api.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Context of an external payment.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class RequestExternalPayment extends BaseRequestPayment {

    private final String title;

    /**
     * Constructor.
     *
     * @param title title of payment
     */
    public RequestExternalPayment(Status status, Error error, String requestId,
                                  BigDecimal contractAmount, String title) {

        super(status, error, requestId, contractAmount);
        this.title = title;
    }

    @Override
    public String toString() {
        return "RequestExternalPayment{" +
                "status=" + getStatus() +
                ", error=" + getError() +
                ", requestId='" + getRequestId() + '\'' +
                ", contractAmount=" + getContractAmount() +
                ", title='" + title + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    /**
     * Requests context of external payment.
     */
    public static class Request implements MethodRequest<RequestExternalPayment> {

        private final String instanceId;
        private final String patternId;
        private final Map<String, String> params;

        /**
         * Use static methods to create
         * {@link com.yandex.money.api.methods.RequestExternalPayment.Request}.
         */
        private Request(String instanceId, String patternId, Map<String, String> params) {
            if (Strings.isNullOrEmpty(instanceId))
                throw new IllegalArgumentException("instanceId is null or empty");
            this.instanceId = instanceId;
            if (Strings.isNullOrEmpty(patternId))
                throw new IllegalArgumentException("patternId is null or empty");
            this.patternId = patternId;

            this.params = params;
        }

        /**
         * Creates instance of payment's request for general purposes. In other words for payments
         * to a specific shop with known parameters.
         *
         * @param instanceId application's instance id
         * @param patternId pattern id of a shop
         * @param paramsShop shop parameters
         * @return new request instance
         */
        public static Request newInstance(String instanceId, String patternId,
                                          Map<String, String> paramsShop) {
            if (paramsShop == null)
                throw new IllegalArgumentException("paramsShop is null or empty");

            return new Request(instanceId, patternId, paramsShop);
        }

        /**
         * Creates instance of request for P2P payments.
         *
         * @param instanceId application's instance id
         * @param p2pParams p2p parameters
         * @return new request instance
         */
        public static Request newInstance(String instanceId, P2pParams p2pParams) {
            if (p2pParams == null)
                throw new IllegalArgumentException("p2pParams is null or empty");

            return new Request(instanceId, P2pParams.PATTERN_ID, p2pParams.makeParams());
        }

        /**
         * Creates instance of request to top up a phone number.
         *
         * @param instanceId application's instance id
         * @param phoneParams phone payment parameters
         * @return new request instance
         */
        public static Request newInstance(String instanceId, PhoneParams phoneParams) {
            if (phoneParams == null)
                throw new IllegalArgumentException("phoneParams is null or empty");

            return new Request(instanceId, PhoneParams.PATTERN_ID, phoneParams.makeParams());
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/request-external-payment");
        }

        @Override
        public RequestExternalPayment parseResponse(InputStream inputStream) {
            return new GsonBuilder().registerTypeAdapter(RequestExternalPayment.class, new JsonDeserializer<RequestExternalPayment>() {
                @Override
                public RequestExternalPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject o = json.getAsJsonObject();
                    return new RequestExternalPayment(
                            Status.parse(JsonUtils.getString(o, MEMBER_STATUS)),
                            Error.parse(JsonUtils.getString(o, MEMBER_ERROR)),
                            JsonUtils.getString(o, MEMBER_REQUEST_ID),
                            JsonUtils.getBigDecimal(o, MEMBER_CONTRACT_AMOUNT),
                            JsonUtils.getString(o, "title"));
                }
            }).create().fromJson(new InputStreamReader(inputStream), RequestExternalPayment.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return new PostRequestBodyBuffer()
                    .addParam("instance_id", instanceId)
                    .addParam("pattern_id", patternId)
                    .addParams(params);
        }
    }
}
