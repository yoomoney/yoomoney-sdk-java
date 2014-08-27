package com.yandex.money.api.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.Language;
import com.yandex.money.api.utils.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Abstract session that provides convenience methods to work with requests.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class AbstractSession {

    private static final Logger LOGGER = Logger.getLogger(OAuth2Session.class.getName());

    protected final ApiClient client;

    private boolean debugLogging = false;

    /**
     * Constructor.
     *
     * @param client API client used to perform operations
     */
    protected AbstractSession(ApiClient client) {
        if (client == null) {
            throw new NullPointerException("client is null");
        }
        this.client = client;
    }

    /**
     * Set requests and responses logging.
     *
     * @param debugLogging {@code true} if logging is required
     */
    public void setDebugLogging(boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    /**
     * Opens connection using specified url.
     *
     * @param url url to open connection
     * @return reference to opened connection
     */
    protected HttpURLConnection openConnection(URL url) {
        OkHttpClient httpClient = client.getHttpClient();
        HttpURLConnection connection = httpClient.open(url);

        connection.setInstanceFollowRedirects(false);
        UserAgent userAgent = client.getUserAgent();
        if (userAgent != null) {
            connection.setRequestProperty(HttpHeaders.USER_AGENT, userAgent.getName());
        }
        Language language = client.getLanguage();
        if (language != null) {
            connection.setRequestProperty(HttpHeaders.ACCEPT_LANGUAGE, language.getIso6391Code());
        }
        connection.setUseCaches(false);

        return connection;
    }

    /**
     * Gets input stream from connection. Logging can be applied if
     * {@link com.yandex.money.api.net.OAuth2Session#setDebugLogging(boolean)} is set to
     * {@code true}.
     *
     * @param connection connection reference
     * @return input stream
     */
    protected InputStream getInputStream(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST ?
                connection.getErrorStream() : connection.getInputStream();
        return debugLogging ? new ResponseLoggingInputStream(stream) : stream;
    }

    /**
     * Processes connection errors.
     *
     * @param connection connection reference
     * @return WWW-Authenticate field of connection
     */
    protected String processError(HttpURLConnection connection) throws IOException {
        String field = connection.getHeaderField(HttpHeaders.WWW_AUTHENTICATE);
        LOGGER.warning("Server has responded with a error: " + getError(connection) + "\n" +
                HttpHeaders.WWW_AUTHENTICATE + ": " + field);
        Streams.readStreamToNull(getInputStream(connection));
        return field;
    }

    private String getError(HttpURLConnection connection) {
        try {
            return "HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
        } catch (IOException e) {
            return "UNKNOWN " + e.toString();
        }
    }
}
