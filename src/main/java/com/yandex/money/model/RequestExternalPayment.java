package com.yandex.money.model;

import com.yandex.money.net.IRequest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class RequestExternalPayment {


    public static class Request implements IRequest<RequestExternalPayment> {

        private String accessToken;
        private String instanceId;
        private String patternId;

        @Override
        public URL requestURL() throws MalformedURLException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public RequestExternalPayment parseResponse(InputStream inputStream) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
