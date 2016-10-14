package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Created on 9/14/16.
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
