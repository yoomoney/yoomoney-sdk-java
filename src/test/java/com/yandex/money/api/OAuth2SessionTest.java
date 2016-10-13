/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.net.FirstApiRequest;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.JsonUtils;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.MimeTypes;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OAuth2SessionTest {

    private final MockWebServer server = new MockWebServer();
    private final ApiClient client = TestEnvironment.createClient();

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
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

    @Test
    public void testBadRequest() throws Exception {
        executeTest(createResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST),
                createRequest(true));
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void testInvalidBadRequest() throws Exception {
        executeTest(createResponseBase().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST),
                createRequest(true));
    }

    @Test(expectedExceptions = InvalidTokenException.class)
    public void testUnauthorized() throws Exception {
        executeTest(createResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED),
                createRequest(true));
    }

    @Test(expectedExceptions = InsufficientScopeException.class)
    public void testForbidden() throws Exception {
        executeTest(createResponse().setResponseCode(HttpURLConnection.HTTP_FORBIDDEN), createRequest(true));
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
        HttpUrl url = server.url("/abc");
        return withParam ? new Mock.Request(url, "param") : new Mock.Request(url);
    }

    private void executeTest(MockResponse response, Mock.Request request) throws Exception {
        server.enqueue(response);
        checkResponse(client.execute(request));
    }

    private void checkResponse(Mock response) {
        Assert.assertEquals(response.code, "ok");
    }

    private static final class Mock {

        final String code;

        Mock(String code) {
            this.code = code;
        }

        static final class Request extends FirstApiRequest<Mock> {

            private final HttpUrl url;

            Request(HttpUrl url) {
                this(url, null);
            }

            Request(HttpUrl url, String param) {
                super(TypeAdapterImpl.INSTANCE);
                this.url = url;
                addParameter("param", param);
            }

            @Override
            protected String requestUrlBase(HostsProvider hostsProvider) {
                return url.toString();
            }
        }

        private static final class TypeAdapterImpl extends BaseTypeAdapter<Mock> {

            static final TypeAdapterImpl INSTANCE = new TypeAdapterImpl();

            @Override
            protected Class<Mock> getType() {
                return Mock.class;
            }

            @Override
            public Mock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {

                JsonObject object = json.getAsJsonObject();
                return new Mock(JsonUtils.getString(object, "code"));
            }

            @Override
            public JsonElement serialize(Mock src, Type typeOfSrc, JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
