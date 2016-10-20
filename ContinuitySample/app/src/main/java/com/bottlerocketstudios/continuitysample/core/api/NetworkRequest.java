package com.bottlerocketstudios.continuitysample.core.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Generic NetworkRequest using OkHttpClient
 */
public interface NetworkRequest {
    HttpUrl getHttpUrl();
    Response performRequest(OkHttpClient okHttpClient) throws IOException;
}
