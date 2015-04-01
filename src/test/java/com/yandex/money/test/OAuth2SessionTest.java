package com.yandex.money.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.net.OnResponseReady;
import com.yandex.money.api.net.PostRequestBodyBuffer;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.MimeTypes;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2SessionTest {

    private final MockWebServer server = new MockWebServer();
    private final OAuth2Session session = new OAuth2Session(new DefaultApiClient("abc", true));

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
    }

    @Test(expectedExceptions = IOException.class)
    public void testWrongContentType() throws Exception {
        executeTest(createResponseBase(), createRequest(true));
    }

    @Test
    public void testWithParams() throws Exception {
        executeTest(createResponse(), createRequest(true));
    }

    @Test
    public void testWithoutParams() throws Exception {
        executeTest(createResponse(), createRequest(false));
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void testBadRequest() throws Exception {
        executeTest(createResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST),
                createRequest(true));
    }

    @Test(expectedExceptions = InvalidTokenException.class)
    public void testUnauthorized() throws Exception {
        executeTest(createResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED),
                createRequest(true));
    }

    @Test(expectedExceptions = InsufficientScopeException.class)
    public void testForbidden() throws Exception {
        executeTest(createResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN),
                createRequest(true));
    }

    @Test
    public void testWithParamsAsync() throws Exception {
        executeTestAsync(createResponse(), createRequest(true));
    }

    @Test
    public void testWithoutParamsAsync() throws Exception {
        executeTestAsync(createResponse(), createRequest(false));
    }

    @Test
    public void testBadRequestAsync() throws Exception {
        executeTestAsync(createResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST),
                createRequest(true));
    }

    @Test
    public void testUnauthorizedAsync() throws Exception {
        executeTestAsync(createResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED),
                createRequest(true));
    }

    @Test
    public void testForbiddenAsync() throws Exception {
        executeTestAsync(createResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN),
                createRequest(true));
    }

    private static MockResponse createResponse() {
        return createResponseBase()
                .addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.Application.JSON);
    }

    private static MockResponse createResponseBase() {
        return new MockResponse()
                .setBody("{\"code\":\"ok\"}");
    }

    private Mock.Request createRequest(boolean withParam) {
        URL url = server.getUrl("/abc");
        return withParam ? new Mock.Request(url, "param") : new Mock.Request(url);
    }

    private void executeTest(MockResponse response, Mock.Request request) throws Exception {
        server.enqueue(response);
        checkResponse(session.execute(request));
    }

    private void executeTestAsync(MockResponse response, Mock.Request request) throws Exception {
        final ThreadSync sync = new ThreadSync();

        server.enqueue(response);
        session.enqueue(request, new OnResponseReady<Mock>() {
            @Override
            public void onFailure(Exception exception) {
                Assert.assertNotNull(exception);
                sync.doNotify();
            }

            @Override
            public void onResponse(Mock response) {
                checkResponse(response);
                sync.doNotify();
            }
        });

        sync.doWait();
    }

    private void checkResponse(Mock response) {
        Assert.assertEquals(response.code, "ok");
    }

    private static final class Mock implements MethodResponse {

        public final String code;

        public Mock(String code) {
            this.code = code;
        }

        public static final class Request implements MethodRequest<Mock> {

            private final URL url;
            private final String param;

            public Request(URL url) {
                this(url, null);
            }

            public Request(URL url, String param) {
                this.url = url;
                this.param = param;
            }

            @Override
            public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
                return url;
            }

            @Override
            public Mock parseResponse(InputStream inputStream) {
                return buildGson().fromJson(new InputStreamReader(inputStream), Mock.class);
            }

            @Override
            public PostRequestBodyBuffer buildParameters() throws IOException {
                return param == null ? null : new PostRequestBodyBuffer().addParam("param", param);
            }

            private static Gson buildGson() {
                return new GsonBuilder()
                        .registerTypeAdapter(Mock.class, new Deserializer())
                        .create();
            }
        }

        private static final class Deserializer implements JsonDeserializer<Mock> {
            @Override
            public Mock deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

                JsonObject object = json.getAsJsonObject();
                return new Mock(JsonUtils.getString(object, "code"));
            }
        }
    }
}
