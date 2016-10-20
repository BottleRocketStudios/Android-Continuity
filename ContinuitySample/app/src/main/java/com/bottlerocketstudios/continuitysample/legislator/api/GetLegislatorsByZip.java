package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Get a collection of legislators for a zip code.
 */
public class GetLegislatorsByZip extends GetLegislatorsLocateBase {
    private final String mZip;

    public GetLegislatorsByZip(String zip) {
        mZip = zip;
    }

    @Override
    public HttpUrl getHttpUrl() {
        return getBaseUrlBuilder()
                .addQueryParameter("zip", mZip)
                .build();
    }
}
