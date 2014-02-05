package com.yandex.money.net;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public interface IRequest<T> {

    String URI_API = "https://money.yandex.ru/api";
    String P2P = "p2p";

    URL requestURL() throws MalformedURLException;

    T parseResponse(InputStream inputStream);
}
