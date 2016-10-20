package com.bottlerocketstudios.continuitysample.core.api;

/**
 * NetworkResponse which is returned by a NetworkExecutor.
 */
public class NetworkResponse<T> {
    private final T mValue;
    private final NetworkResponseStatus mNetworkResponseStatus;

    public NetworkResponse(T value, NetworkResponseStatus networkResponseStatus) {
        mValue = value;
        mNetworkResponseStatus = networkResponseStatus;
    }

    public T getValue() {
        return mValue;
    }

    public NetworkResponseStatus getNetworkResponseStatus() {
        return mNetworkResponseStatus;
    }

    public boolean isSuccess() {
        return getNetworkResponseStatus().equals(NetworkResponseStatus.SUCCESS);
    }
}
