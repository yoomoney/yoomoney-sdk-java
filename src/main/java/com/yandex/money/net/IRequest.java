package com.yandex.money.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public interface IRequest<T> {

//    String URI_API = "https://money.yandex.ru/api";
//    String URI_API = "https://baku.yandex.ru/api";
    String URI_API = "https://polo.yandex.ru/api";

    URL requestURL() throws MalformedURLException;

    T parseResponse(InputStream inputStream);

    PostRequestBodyBuffer buildParameters() throws IOException;

    void writeHeaders(HttpURLConnection connection);
}
