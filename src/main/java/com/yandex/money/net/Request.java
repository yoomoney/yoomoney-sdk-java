package com.yandex.money.net;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public interface Request<T> {

    URL requestURL() throws MalformedURLException;

    T parseResponse(InputStream inputStream);
}
