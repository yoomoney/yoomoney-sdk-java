package com.yandex.money.test.wrapper;

import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.HostsProvider;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by akintsev on 6/14/2016.
 */
public class CustomPathRequestWrapper <C, T extends ApiRequest<C>> implements ApiRequest<C> {

    private final T originalRequest;
    private final String customPath;

    /**
     * @param originalRequest - request
     * @param customPath - stub path
     */
    public CustomPathRequestWrapper(T originalRequest,String customPath){
        this.originalRequest = originalRequest;
        this.customPath = customPath;
    }

    @Override
    public String requestUrl(HostsProvider hostsProvider) {

        return hostsProvider.getMoney() + customPath;
    }


    @Override
    public ApiRequest.Method getMethod() {
        return originalRequest.getMethod();
    }

    @Override
    public Map<String, String> getHeaders() {
        return originalRequest.getHeaders();
    }

    @Override
    public Map<String, String> getParameters() {
        return originalRequest.getParameters();
    }

    @Override
    public C parseResponse(InputStream inputStream) {
        return originalRequest.parseResponse(inputStream);
    }
}
