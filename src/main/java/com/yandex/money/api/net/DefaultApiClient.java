package com.yandex.money.api.net;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.Language;
import com.yandex.money.api.utils.Strings;

import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Default implementation of {@link com.yandex.money.api.net.ApiClient} interface.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class DefaultApiClient implements ApiClient {

    private static final long DEFAULT_TIMEOUT = 30;

    private final String id;
    private final OkHttpClient httpClient;
    private final HostsProvider hostsProvider;

    private String platform = "Java";

    /**
     * Constructor.
     *
     * @param clientId client id to use
     */
    public DefaultApiClient(String clientId) {
        if (clientId == null) {
            throw new NullPointerException("client id is null");
        }
        id = clientId;
        httpClient = createHttpClient();
        hostsProvider = new HostsProvider(false);
    }

    /**
     * Constructor.
     *
     * @param clientId client id to use
     * @param debugLogging {@code true} if logging is required
     */
    public DefaultApiClient(String clientId, boolean debugLogging) {
        this(clientId);
        if (debugLogging) {
            SSLSocketFactory sslSocketFactory = createSslSocketFactory();
            httpClient.setSslSocketFactory(new WireLoggingSocketFactory(sslSocketFactory));
        }
    }

    /**
     * Constructor.
     * <p/>
     * If {@code platform} parameter is null or empty default value will be used. No exception will
     * be thrown.
     *
     * @param clientId client id to use
     * @param debugLogging {@code true} if logging is required
     * @param platform the name of a platform client is running on
     */
    public DefaultApiClient(String clientId, boolean debugLogging, String platform) {
        this(clientId, debugLogging);
        if (!Strings.isNullOrEmpty(platform)) {
            this.platform = platform;
        }
    }

    @Override
    public String getClientId() {
        return id;
    }

    @Override
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public HostsProvider getHostsProvider() {
        return hostsProvider;
    }

    @Override
    public UserAgent getUserAgent() {
        return new DefaultUserAgent(platform);
    }

    @Override
    public Language getLanguage() {
        return Language.getDefault();
    }

    private static OkHttpClient createHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectionPool(new ConnectionPool(4, 10 * 60 * 1000L));
        client.setFollowSslRedirects(false);
        client.setFollowRedirects(false);
        return client;
    }

    private static SSLSocketFactory createSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
            return context.getSocketFactory();
        } catch (GeneralSecurityException exception) {
            throw new RuntimeException(exception);
        }
    }
}
