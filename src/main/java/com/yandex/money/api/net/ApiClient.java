package com.yandex.money.api.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.Language;

/**
 * Yandex.Money API client. Provides necessary information for sessions.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface ApiClient {
    /**
     * @return client id of an application
     */
    String getId();

    /**
     * @return HTTP client to use when executing API requests
     */
    OkHttpClient getHttpClient();

    /**
     * @return hosts provider
     */
    HostsProvider getHostsProvider();

    /**
     * @return specific HTTP user agent
     */
    UserAgent getUserAgent();

    /**
     * @return language of API responses
     */
    Language getLanguage();
}
