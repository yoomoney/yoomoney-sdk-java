package com.yandex.money.api.net;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.MimeTypes;
import com.yandex.money.api.utils.Streams;
import com.yandex.money.api.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * OAuth2 session that can be used to perform API requests and retrieve responses.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2Session extends AbstractSession {

    private String accessToken;

    /**
     * Constructor.
     *
     * @param client API client used to perform operations
     */
    public OAuth2Session(ApiClient client) {
        super(client);
    }

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
                connection.setRequestMethod("POST");
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
     * Convenience method to create {@link com.yandex.money.api.net.OAuth2Authorization} object for
     * user authentication.
     *
     * @return authorization parameters
     */
    public OAuth2Authorization createOAuth2Authorization() {
        return new OAuth2Authorization(client);
    }

    private boolean isJsonType(HttpURLConnection connection) {
        String field = connection.getHeaderField(HttpHeaders.CONTENT_TYPE);
        return field != null && field.startsWith(MimeTypes.Application.JSON);
    }
}
