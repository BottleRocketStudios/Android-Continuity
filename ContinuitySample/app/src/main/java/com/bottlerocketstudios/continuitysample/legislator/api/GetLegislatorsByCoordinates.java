package com.bottlerocketstudios.continuitysample.legislator.api;

import android.location.Location;

import okhttp3.HttpUrl;

/**
 * Created on 9/14/16.
 */
public class GetLegislatorsByCoordinates extends GetLegislatorsLocateBase {

    private final Location mLocation;

    public GetLegislatorsByCoordinates(Location location) {
        mLocation = location;
    }

    @Override
    public HttpUrl getHttpUrl() {
        return getBaseUrlBuilder()
                .addQueryParameter("latitude", String.valueOf(mLocation.getLatitude()))
                .addQueryParameter("longitude", String.valueOf(mLocation.getLongitude()))
                .build();
    }
}
