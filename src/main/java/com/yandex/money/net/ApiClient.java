package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.utils.Language;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface ApiClient {
    String getId();
    OkHttpClient getHttpClient();
    HostsProvider getHostsProvider();
    String getUserAgent();
    Language getLanguage();
}
