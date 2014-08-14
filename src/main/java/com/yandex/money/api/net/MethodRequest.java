package com.yandex.money.api.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public interface MethodRequest<T> {

    URL requestURL(HostsProvider hostsProvider) throws MalformedURLException;

    T parseResponse(InputStream inputStream);

    PostRequestBodyBuffer buildParameters() throws IOException;
}
