package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Created on 9/14/16.
 */
public class GetLegislatorsByBioguideId extends GetLegislatorsBase {

    private final String mBioguideId;

    public GetLegislatorsByBioguideId(String bioguideId) {
        mBioguideId = bioguideId;
    }

    @Override
    public HttpUrl getHttpUrl() {
        return getBaseUrlBuilder()
                .addQueryParameter("bioguide_id", mBioguideId)
                .build();
    }

}
