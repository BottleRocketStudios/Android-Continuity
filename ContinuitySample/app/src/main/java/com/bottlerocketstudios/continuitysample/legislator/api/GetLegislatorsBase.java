package com.bottlerocketstudios.continuitysample.legislator.api;

import com.bottlerocketstudios.continuitysample.core.api.SunlightRequest;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Base request for Legislator endpoint.
 */
public abstract class GetLegislatorsBase extends SunlightRequest {

    @Override
    public Response performRequest(OkHttpClient okHttpClient) throws IOException {
        Request request = addApiKeyHeader(new Request.Builder())
                .url(getHttpUrl())
                .get()
                .build();

        return okHttpClient.newCall(request).execute();
    }

    @Override
    protected HttpUrl.Builder getBaseUrlBuilder() {
        return super.getBaseUrlBuilder()
                .addPathSegment("legislators");
    }
}
