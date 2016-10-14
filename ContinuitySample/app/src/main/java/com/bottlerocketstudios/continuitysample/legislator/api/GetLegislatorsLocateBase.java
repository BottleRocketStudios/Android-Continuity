package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Created on 9/14/16.
 */
public abstract class GetLegislatorsLocateBase extends GetLegislatorsBase {
    @Override
    protected HttpUrl.Builder getBaseUrlBuilder() {
        return super.getBaseUrlBuilder()
                .addPathSegment("locate");
    }
}
