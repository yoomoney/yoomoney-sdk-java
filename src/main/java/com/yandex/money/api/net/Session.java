package com.yandex.money.api.net;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Request;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.Language;

import java.util.Map;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * @author Arutyun Agababyan (agababyanh@yamoney.ru)
 */
public class Session extends AbstractSession<BaseApiRequest> {

    private final CacheControl cacheControl = new CacheControl.Builder().noCache().build();

    /**
     * Constructor.
     *
     * @param client API client used to perform operations
     */
    protected Session(ApiClient client) {
        super(client);
    }

    @Override
    protected Request.Builder prepareRequestBuilder(BaseApiRequest request) {
        checkNotNull(request, "request");

        Request.Builder builder = new Request.Builder()
                .cacheControl(cacheControl);

        UserAgent userAgent = client.getUserAgent();
        if (userAgent != null) {
            builder.addHeader(HttpHeaders.USER_AGENT, userAgent.getName());
        }

        Language language = client.getLanguage();
        if (language != null) {
            builder.addHeader(HttpHeaders.ACCEPT_LANGUAGE, language.iso6391Code);
        }
        final Map<String, String> headers = request.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                builder.addHeader(entry.getKey(), value);
            }
        }

        ParametersBuffer parametersBuffer = new ParametersBuffer()
                .setParams(request.getParameters());

        switch (request.getMethod()) {
            case GET: {
                builder.url(request.requestUrl(client.getHostsProvider()) +
                        parametersBuffer.prepareGet());
                break;
            }
            case POST: {
                builder.url(request.requestUrl(client.getHostsProvider()))
                        .post(parametersBuffer.prepareBody());
                break;
            }
            default:
                throw new UnsupportedOperationException("method " + request.getMethod() + " is not supported");
        }

        return builder;
    }
}
