package com.yandex.money.model;

import com.yandex.money.Utils;
import com.yandex.money.net.IRequest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 */
public class ProcessExternalPayment {

    private String status;
    private String error;
    private String acs_uri;
    private Map<String, String> acs_params;
    private String money_source;
    private Long next_retry;
    private String invoiceId;

    public static class Request implements IRequest<ProcessExternalPayment> {

        private String accessToken;
        private String instanceId;
        private String requestId;
        private String extAuthSuccessUri;
        private String extAuthFailUri;
        private boolean requestToken;

        public Request(String accessToken, String instanceId, String requestId, String extAuthSuccessUri,
                       String extAuthFailUri, boolean requestToken) {
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

            this.requestToken = requestToken;
        }

        @Override
        public URL requestURL() throws MalformedURLException {
            return new URL(URI_API + "/process-external-payment");
        }

        @Override
        public ProcessExternalPayment parseResponse(InputStream inputStream) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
