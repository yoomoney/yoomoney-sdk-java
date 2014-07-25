package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface ApiClient {
    String getId();
    OkHttpClient getHttpClient();
    HostsProvider getHostsProvider();
    String getUserAgent();
    String getLanguage();
}
