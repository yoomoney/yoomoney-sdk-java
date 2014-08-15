package com.yandex.money.api.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.Language;
import com.yandex.money.api.utils.MimeTypes;
import com.yandex.money.api.utils.Streams;
import com.yandex.money.api.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * OAuth2 session that can be used to perform API requests and retrieve responses.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2Session implements Session {

    private static final Logger LOGGER = Logger.getLogger(OAuth2Session.class.getName());

    protected final ApiClient client;

    private SSLSocketFactory sslSocketFactory;
    private String accessToken;
    private boolean debugLogging = false;

    /**
     * Constructor.
     *
     * @param client API client used to perform operations
     */
    public OAuth2Session(ApiClient client) {
        if (client == null) {
            throw new NullPointerException("client is null");
        }
        this.client = client;
    }

    @Override
    public <T> T execute(MethodRequest<T> request) throws IOException, InvalidRequestException,
            InvalidTokenException, InsufficientScopeException {

        if (request == null) {
            throw new NullPointerException("request is null");
        }

        final HostsProvider hostsProvider = client.getHostsProvider();
        final HttpURLConnection connection = openConnection(request.requestURL(hostsProvider));

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            PostRequestBodyBuffer parameters = request.buildParameters();
            if (parameters == null) {
                connection.setRequestProperty(HttpHeaders.CONTENT_LENGTH, "0");
            } else {
                parameters.setHttpHeaders(connection);
            }

            if (isAuthorized()) {
                connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            }

            if (parameters != null) {
                outputStream = connection.getOutputStream();
                parameters.write(outputStream);
                outputStream.close();
                outputStream = null;
            }

            switch (connection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    inputStream = getInputStream(connection);
                    if (isJsonType(connection)) {
                        return request.parseResponse(inputStream);
                    } else {
                        Streams.readStreamToNull(inputStream);
                        throw new IOException("Server has responded with a wrong content type");
                    }
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new InvalidRequestException(processError(connection));
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    throw new InvalidTokenException(processError(connection));
                case HttpURLConnection.HTTP_FORBIDDEN:
                    throw new InsufficientScopeException(processError(connection));
                default:
                    throw new IOException(processError(connection));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * Sets access token to perform authorized operations. Can be set to {@code null}, if no
     * access token is required to execute a request.
     *
     * @param accessToken access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Checks if session is authorized.
     *
     * @return {@code true} if authorized
     */
    public boolean isAuthorized() {
        return !Strings.isNullOrEmpty(accessToken);
    }

    /**
     * Set requests and responses logging.
     *
     * @param debugLogging {@code true} if logging is required
     */
    public void setDebugLogging(boolean debugLogging) {
        this.debugLogging = debugLogging;
        this.sslSocketFactory = createSslSocketFactory();
        if (debugLogging) {
            sslSocketFactory = new WireLoggingSocketFactory(sslSocketFactory);
        }
    }

    /**
     * Convenience method to create {@link com.yandex.money.api.net.OAuth2Authorization} object for
     * user authentication.
     *
     * @return authorization parameters
     */
    public OAuth2Authorization createOAuth2Authorization() {
        return new OAuth2Authorization(client);
    }

    /**
     * Opens connection using specified url.
     *
     * @param url url to open connection
     * @return reference to opened connection
     */
    protected HttpURLConnection openConnection(URL url) {
        OkHttpClient httpClient = getHttpClient();
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
        return debugLogging ? new WireLoggingInputStream(stream) : stream;
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

    private static SSLSocketFactory createSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
            return context.getSocketFactory();
        } catch (GeneralSecurityException exception) {
            throw new RuntimeException(exception);
        }
    }

    private OkHttpClient getHttpClient() {
        OkHttpClient httpClient = client.getHttpClient();
        if (sslSocketFactory != null) {
            httpClient.setSslSocketFactory(sslSocketFactory);
        }
        return httpClient;
    }

    private boolean isJsonType(HttpURLConnection connection) {
        String field = connection.getHeaderField(HttpHeaders.CONTENT_TYPE);
        return field != null && field.startsWith(MimeTypes.Application.JSON);
    }

    private String getError(HttpURLConnection connection) {
        try {
            return "HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
        } catch (IOException e) {
            return "UNKNOWN " + e.toString();
        }
    }
}
