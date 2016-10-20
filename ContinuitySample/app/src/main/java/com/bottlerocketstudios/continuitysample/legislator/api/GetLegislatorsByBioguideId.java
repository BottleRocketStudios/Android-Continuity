package com.bottlerocketstudios.continuitysample.legislator.api;

import okhttp3.HttpUrl;

/**
 * Get a specific legislator identified by bioguide_id
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
