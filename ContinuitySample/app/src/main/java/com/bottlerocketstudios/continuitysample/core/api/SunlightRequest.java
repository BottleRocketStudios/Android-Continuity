package com.bottlerocketstudios.continuitysample.core.api;

import com.bottlerocketstudios.continuitysample.BuildConfig;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created on 9/14/16.
 */
public abstract class SunlightRequest implements NetworkRequest {

    public static final String HEADER_API_KEY = "X-APIKEY";
    public static final HttpUrl BASE_URL = HttpUrl.parse("https://congress.api.sunlightfoundation.com");

    protected HttpUrl.Builder getBaseUrlBuilder() {
        return BASE_URL.newBuilder();
    }

    protected String getApiKey() {
        return BuildConfig.SUNLIGHT_API_KEY;
    }

    protected Request.Builder addApiKeyHeader(Request.Builder builder) {
        return builder.addHeader(HEADER_API_KEY, getApiKey());
    }

}
