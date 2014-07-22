package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;
import com.yandex.money.utils.HttpHeaders;
import com.yandex.money.utils.MimeTypes;
import com.yandex.money.utils.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2Session implements Session {

    private static final Logger LOGGER = Logger.getLogger(OAuth2Session.class.getName());

    private final ApiClient client;

    private String token;
    private boolean debugLogging = false;

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

        final OkHttpClient httpClient = client.getHttpClient();
        final HostsProvider hostsProvider = client.getHostsProvider();
        final HttpURLConnection connection = httpClient.open(request.requestURL(hostsProvider));
        connection.setInstanceFollowRedirects(false);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            PostRequestBodyBuffer parameters = request.buildParameters();
            if (parameters == null) {
                connection.setRequestProperty(HttpHeaders.CONTENT_LENGTH, "0");
            } else {
                parameters.setHttpHeaders(connection);
            }

            connection.setRequestProperty(HttpHeaders.USER_AGENT, client.getUserAgent());
            connection.setRequestProperty(HttpHeaders.ACCEPT_LANGUAGE, client.getLanguage());

            if (isAuthorized()) {
                connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            }
            connection.setUseCaches(false);

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

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthorized() {
        return token != null && token.length() > 0;
    }

    public void setDebugLogging(boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    public OAuth2Authorization createOAuth2Authorization() {
        return new OAuth2Authorization(client.getHostsProvider());
    }

    private boolean isJsonType(HttpURLConnection connection) {
        String field = connection.getHeaderField(HttpHeaders.CONTENT_TYPE);
        return field != null && field.startsWith(MimeTypes.Application.JSON);
    }

    private InputStream getInputStream(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST ?
                connection.getErrorStream() : connection.getInputStream();
        return debugLogging ? new WireLoggingInputStream(stream) : stream;
    }

    private String processError(HttpURLConnection connection) throws IOException {
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
