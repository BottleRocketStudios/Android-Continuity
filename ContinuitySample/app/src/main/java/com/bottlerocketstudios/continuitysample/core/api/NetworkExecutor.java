package com.bottlerocketstudios.continuitysample.core.api;

import com.bottlerocketstudios.continuitysample.core.util.LogIt;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created on 9/14/16.
 */
public class NetworkExecutor<T> {
    private static final String TAG = NetworkExecutor.class.getSimpleName();

    public static <T> NetworkResponse<T> execute(OkHttpClient okHttpClient, NetworkRequest networkRequest, ResponseHandler<T> responseHandler) {
        NetworkExecutor<T> networkExecutor = new NetworkExecutor<>();
        return networkExecutor.executeRequest(okHttpClient, networkRequest, responseHandler);
    }

    private NetworkResponse<T> executeRequest(OkHttpClient okHttpClient, NetworkRequest networkRequest, ResponseHandler<T> responseHandler) {
        Response response = null;
        try {
            response = networkRequest.performRequest(okHttpClient);
        } catch (IOException e) {
            LogIt.e(TAG, "Caught java.io.IOException", e);
        }

        if (response == null) {
            return new NetworkResponse<>(null, NetworkResponseStatus.IO_ERROR);
        }

        NetworkResponseStatus networkResponseStatus = responseHandler.processResponseStatus(response);
        T result = null;

        if (networkResponseStatus.equals(NetworkResponseStatus.SUCCESS)) {
            result = responseHandler.processResponse(response);
        }

        if (result == null) {
            return new NetworkResponse<>(null, NetworkResponseStatus.PARSE_ERROR);
        }

        return new NetworkResponse<>(result, networkResponseStatus);
    }
}
