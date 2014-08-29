package com.yandex.money.api.net;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkAuthenticator;
import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.Language;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
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
        return new UserAgent() {
            @Override
            public String getName() {
                return "Yandex.Money.SDK/Java";
            }
        };
    }

    @Override
    public Language getLanguage() {
        return Language.getDefault();
    }

    private static OkHttpClient createHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setAuthenticator(NULL_AUTHENTICATOR);
        client.setConnectionPool(new ConnectionPool(4, 10 * 60 * 1000L));
        client.setFollowProtocolRedirects(false);
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

    private static OkAuthenticator NULL_AUTHENTICATOR = new OkAuthenticator() {
        @Override
        public OkAuthenticator.Credential authenticate(
                Proxy proxy, URL url, List<Challenge> challenges) throws IOException {
            return null;
        }

        @Override
        public Credential authenticateProxy(
                Proxy proxy, URL url, List<Challenge> challenges) throws IOException {
            return null;
        }
    };
}
