package com.yandex.money.api.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * API methods implement this interface.
 *
 * @param <T> response
 */
public interface MethodRequest<T> {

    /**
     * Builds URL with using specified hosts provider
     *
     * @param hostsProvider hosts provider
     * @return complete URL
     * @throws MalformedURLException if URL is malformed
     */
    URL requestURL(HostsProvider hostsProvider) throws MalformedURLException;

    /**
     * Parses API response from stream.
     *
     * @param inputStream input stream
     * @return response
     */
    T parseResponse(InputStream inputStream);

    /**
     * Builds post parameters to use when posting a request. Can be null.
     *
     * @return buffer of parameters
     */
    PostRequestBodyBuffer buildParameters() throws IOException;
}
