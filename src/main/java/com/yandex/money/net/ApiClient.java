package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface ApiClient {
    OkHttpClient getHttpClient();
    String getUserAgent();
    String getLanguage();
}
