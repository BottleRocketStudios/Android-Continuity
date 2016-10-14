package com.bottlerocketstudios.continuitysample.core.api;

import okhttp3.Response;

/**
 * Created on 9/14/16.
 */
public interface ResponseHandler<T> {
    T processResponse(Response response);
    NetworkResponseStatus processResponseStatus(Response response);
}
