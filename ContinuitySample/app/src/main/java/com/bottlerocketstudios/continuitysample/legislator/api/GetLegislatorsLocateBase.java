package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Base request for location based legislator searches.
 */
public abstract class GetLegislatorsLocateBase extends GetLegislatorsBase {
    @Override
    protected HttpUrl.Builder getBaseUrlBuilder() {
        return super.getBaseUrlBuilder()
                .addPathSegment("locate");
    }
}
