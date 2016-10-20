package com.bottlerocketstudios.continuitysample.core.model;

import com.bottlerocketstudios.continuitysample.core.api.NetworkResponseStatus;

/**
 * Status of response from a UseCase (Agent)
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
