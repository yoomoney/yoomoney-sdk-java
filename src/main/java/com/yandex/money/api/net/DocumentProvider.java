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

import com.squareup.okhttp.Response;
import com.yandex.money.api.exceptions.ResourceNotFoundException;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.model.showcase.ShowcaseTypeAdapter;
import com.yandex.money.api.util.HttpHeaders;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

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

    public <T> HttpResourceResponse<T> fetch(ApiRequest<T> request) throws IOException, ResourceNotFoundException {
        return parseResponse(request, prepareCall(request).execute());
    }

    /**
     * Obtains {@link Showcase} instance from remote server in blocking way.
     *
     * @param request {@link ApiRequest} instance
     * @return {@link ShowcaseContext} in initial state.
     * @throws IOException               various I/O errors (timeouts, etc.)
     * @throws ResourceNotFoundException 404 status code feedback
     */
    public ShowcaseContext getShowcase(ApiRequest<Showcase> request) throws IOException, ResourceNotFoundException {
        return getShowcaseInner(request, 1);
    }

    /**
     * Submits {@link Showcase} wrapped by {@link ShowcaseContext} to next step.
     *
     * @param showcaseContext current {@link Showcase} state
     * @return mutable instance of input parameter
     * @throws IOException               various I/O errors (timeouts, etc.)
     * @throws ResourceNotFoundException 404 status code feedback
     */
    public ShowcaseContext submitShowcase(ShowcaseContext showcaseContext)
            throws IOException, ResourceNotFoundException {
        return parseResponse(prepareCall(showcaseContext.createRequest()).execute(),
                showcaseContext);
    }

    private ShowcaseContext getShowcaseInner(ApiRequest<Showcase> request, int requestRemained)
            throws IOException, ResourceNotFoundException {
        return parseResponse(prepareCall(request).execute(), request, requestRemained);
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

    private void throwResourceNotFoundException(Response response) throws IOException, ResourceNotFoundException {
        processError(response);
        throw new ResourceNotFoundException(response.request().url());
    }

    private static final class CopyShowcaseRequest extends GetRequest<Showcase> {

        private final String url;

        private CopyShowcaseRequest(String url, ApiRequest<Showcase> request) {
            super(ShowcaseTypeAdapter.getInstance());
            this.url = checkNotEmpty(url, "url");
            addHeaders(checkNotNull(request, "request").getHeaders());
            setBody(request.getBody());
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return url;
        }
    }
}
