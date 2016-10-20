package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Get a collection of legislators with a name that contains the search string.
 */
public class GetLegislatorsByName extends GetLegislatorsBase {
    private final String mName;

    public GetLegislatorsByName(String name) {
        mName = name;
    }

    @Override
    public HttpUrl getHttpUrl() {
        return getBaseUrlBuilder()
                .addQueryParameter("query", mName)
                .build();
    }

}
