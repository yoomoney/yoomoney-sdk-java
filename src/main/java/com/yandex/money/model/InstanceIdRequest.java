package com.yandex.money.model;

import com.yandex.money.net.Request;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class InstanceIdRequest implements Request<InstanceIdRequest.Response> {

    private String clientId;

    @Override
    public URL requestURL() throws MalformedURLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response parseResponse(InputStream inputStream) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public class Response {

    }
}
