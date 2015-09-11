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

package com.yandex.money.api.net;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yandex.money.api.exceptions.ResourceNotFoundException;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.typeadapters.showcase.ShowcaseTypeAdapter;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.Strings;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * Provides various resources from Yandex.Money server.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class DocumentProvider extends AbstractSession {

    /**
     * Constructor.
     *
     * @param client API client used to perform operations
     */
    public DocumentProvider(ApiClient client) {
        super(client);
    }

    public <T> HttpResourceResponse<T> fetch(ApiRequest<T> request)
            throws IOException, ResourceNotFoundException {
        return parseResponse(request, prepareCall(request).execute());
    }

    public <T> Call fetch(final ApiRequest<T> request,
                          final OnResponseReady<HttpResourceResponse<T>> callback)
            throws MalformedURLException {

        Call call = prepareCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    callback.onResponse(parseResponse(request, response));
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        });
        return call;
    }

    /**
     * Fetches {@link Showcase} instance from remote server.
     *
     * @param request {@link ApiRequest} instance
     * @return {@link ShowcaseContext} in initial state.
     * @throws IOException               various I/O errors (timeouts, etc.)
     * @throws ResourceNotFoundException 404 status code
     */
    public ShowcaseContext getShowcase(ApiRequest<Showcase> request)
            throws IOException, ResourceNotFoundException {
        return getShowcaseInner(request, 1);
    }

    /**
     * Fetches {@link Showcase} instance from remote server.
     *
     * @param request  {@link ApiRequest} instance
     * @param callback success/fail handler.
     * @return {@link Call} instance in started state.
     * @throws MalformedURLException
     */
    public Call getShowcase(ApiRequest<Showcase> request, OnResponseReady<ShowcaseContext> callback)
            throws MalformedURLException {
        return getShowcaseInner(request, callback, 1);
    }

    public ShowcaseContext submitShowcase(ShowcaseContext showcaseContext)
            throws IOException, ResourceNotFoundException {

        return parseResponse(prepareCall(showcaseContext.createRequest()).execute(),
                showcaseContext);
    }

    public Call submitShowcase(final ShowcaseContext showcaseContext,
                               final OnResponseReady<ShowcaseContext> callback) {

        Call call = prepareCall(showcaseContext.createRequest());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    parseResponse(response, showcaseContext);
                    callback.onResponse(showcaseContext);
                } catch (ResourceNotFoundException e) {
                    callback.onFailure(e);
                }
            }
        });
        return call;
    }

    private ShowcaseContext getShowcaseInner(ApiRequest<Showcase> request, int requestRemained)
            throws IOException, ResourceNotFoundException {
        return parseResponse(prepareCall(request).execute(), request, requestRemained);
    }

    private Call getShowcaseInner(final ApiRequest<Showcase> request,
                                  final OnResponseReady<ShowcaseContext> callback,
                                  final int requestRemained) {

        Call call = prepareCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    callback.onResponse(parseResponse(response, request, requestRemained));
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        });
        return call;
    }

    private <T> HttpResourceResponse<T> parseResponse(ApiRequest<T> request, Response response)
            throws IOException, ResourceNotFoundException {

        InputStream inputStream = null;
        try {
            int responseCode = response.code();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_NOT_MODIFIED:
                    DateTime lastModified = parseDateHeader(response, HttpHeaders.LAST_MODIFIED);
                    DateTime expires = parseDateHeader(response, HttpHeaders.EXPIRES);

                    HttpResourceResponse.ResourceState resourceState =
                            HttpResourceResponse.ResourceState.NOT_MODIFIED;
                    T document = null;
                    String contentType = null;

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        resourceState = HttpResourceResponse.ResourceState.DOCUMENT;
                        contentType = response.header(HttpHeaders.CONTENT_TYPE);
                        inputStream = getInputStream(response);
                        document = request.parseResponse(inputStream);
                    }

                    return new HttpResourceResponse<>(resourceState, contentType, lastModified,
                            expires, document);
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throwResourceNotFoundException(response);
                default:
                    throw new IOException(processError(response));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private ShowcaseContext parseResponse(Response response, ApiRequest<Showcase> request,
                                          int requestRemained)
            throws IOException, ResourceNotFoundException {

        InputStream inputStream = null;
        try {
            int responseCode = response.code();
            switch (responseCode) {
                case HttpURLConnection.HTTP_MULT_CHOICE:
                    DateTime dateModified = parseDateHeader(response, HttpHeaders.LAST_MODIFIED);
                    final String location = response.header(HttpHeaders.LOCATION);

                    inputStream = getInputStream(response);
                    Showcase showcase = ShowcaseTypeAdapter.getInstance().fromJson(inputStream);
                    ShowcaseContext showcaseContext = new ShowcaseContext(showcase, location,
                            dateModified);
                    showcaseContext.setState(ShowcaseContext.State.HAS_NEXT_STEP);
                    return showcaseContext;
                case HttpURLConnection.HTTP_MOVED_PERM:
                    if (requestRemained > 0) {
                        String url = response.header(HttpHeaders.LOCATION);
                        return getShowcaseInner(new CopyShowcaseRequest(url, request),
                                --requestRemained);
                    } else {
                        throw new IOException("recurrent call of getShowcase()");
                    }
                case HttpURLConnection.HTTP_NOT_MODIFIED: {
                    return new ShowcaseContext(ShowcaseContext.State.NOT_MODIFIED);
                }
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throwResourceNotFoundException(response);
                default:
                    throw new IOException(processError(response));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private ShowcaseContext parseResponse(Response response, ShowcaseContext showcaseContext)
            throws IOException, ResourceNotFoundException {

        InputStream inputStream = null;
        try {
            int responseCode = response.code();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    inputStream = getInputStream(response);
                    showcaseContext.setParams(inputStream);
                    showcaseContext.setState(ShowcaseContext.State.COMPLETED);
                    return showcaseContext;
                case HttpURLConnection.HTTP_MULT_CHOICE:
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    final String newLocation = response.header(HttpHeaders.LOCATION);

                    inputStream = getInputStream(response);
                    Showcase newShowcase = ShowcaseTypeAdapter.getInstance().fromJson(inputStream);

                    ShowcaseContext.Step step = new ShowcaseContext.Step(newShowcase,
                            newLocation == null ? null : newLocation);
                    if (responseCode == HttpURLConnection.HTTP_MULT_CHOICE) {
                        showcaseContext.pushCurrentStep(step);
                        showcaseContext.setState(ShowcaseContext.State.HAS_NEXT_STEP);
                    } else {
                        showcaseContext.setCurrentStep(step);
                        showcaseContext.setState(ShowcaseContext.State.INVALID_PARAMS);
                    }
                    return showcaseContext;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throwResourceNotFoundException(response);
                default:
                    throw new IOException(processError(response));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private DateTime parseDateHeader(Response response, String header) {
        String dateHeader = response.header(header);
        return dateHeader == null || dateHeader.isEmpty() ? new DateTime() :
                BaseApiRequest.DATE_TIME_FORMATTER.parseDateTime(dateHeader);
    }

    private void throwResourceNotFoundException(Response response)
            throws IOException, ResourceNotFoundException {
        processError(response);
        throw new ResourceNotFoundException(response.request().url());
    }

    private static final class CopyShowcaseRequest extends GetRequest<Showcase> {

        private final String url;

        protected CopyShowcaseRequest(String url, ApiRequest<Showcase> request) {
            super(Showcase.class, ShowcaseTypeAdapter.getInstance());
            if (Strings.isNullOrEmpty(url)) {
                throw new IllegalArgumentException("url is null or empty");
            }
            if (request == null) {
                throw new NullPointerException("request is null or empty");
            }

            this.url = url;
            addHeaders(request.getHeaders());
            addParameters(request.getParameters());
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return url;
        }
    }
}
