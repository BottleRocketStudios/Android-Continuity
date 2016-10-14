package com.bottlerocketstudios.continuitysample.core.api;

import java.util.Locale;

import okhttp3.HttpUrl;

/**
 * Created on 10/3/16.
 */

public abstract class GovtrackRequest implements NetworkRequest {
    public static final HttpUrl BASE_URL = HttpUrl.parse("https://www.govtrack.us/api/v2");
    public static final HttpUrl BASE_IMAGE_URL = HttpUrl.parse("https://www.govtrack.us/data/photos");

    private static final String IMAGE_URL_FORMAT_200PX = "%1$s-200px.jpeg";

    protected HttpUrl.Builder getBaseUrlBuilder() {
        return BASE_URL.newBuilder();
    }

    public static String getImageUrl(String govtrackId) {
        HttpUrl httpUrl = BASE_IMAGE_URL.newBuilder().addPathSegment(String.format(Locale.US, IMAGE_URL_FORMAT_200PX, govtrackId)).build();
        return httpUrl.toString();
    }
}
