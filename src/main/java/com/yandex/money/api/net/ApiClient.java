package com.yandex.money.api.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.Language;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface ApiClient {
    String getId();
    OkHttpClient getHttpClient();
    HostsProvider getHostsProvider();
    UserAgent getUserAgent();
    Language getLanguage();
}
