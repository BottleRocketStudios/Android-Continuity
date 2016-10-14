package com.bottlerocketstudios.continuitysample.core.model;

import com.bottlerocketstudios.continuitysample.core.api.NetworkResponseStatus;

/**
 * Created on 9/14/16.
 */
public enum ResponseStatus {
    NETWORK_ERROR,
    INVALID_DATA,
    SUCCESS;

    public static ResponseStatus translateNetworkStatus(NetworkResponseStatus networkResponseStatus) {
        switch (networkResponseStatus) {
            case PARSE_ERROR:
                return INVALID_DATA;
            case SUCCESS:
                return SUCCESS;
            default:
                return NETWORK_ERROR;
        }
    }
}
