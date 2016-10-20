package com.bottlerocketstudios.continuitysample.core.api;

import okhttp3.Response;

/**
 * Process the result of a NetworkRequest
 */
public interface ResponseHandler<T> {
    T processResponse(Response response);
    NetworkResponseStatus processResponseStatus(Response response);
}
