package com.bottlerocketstudios.continuitysample.core.model;

/**
 * Generic container for responses from UseCase (Agent)
 */
public class ResponseContainer<T> {
    private final T mValue;
    private final ResponseStatus mResponseStatus;

    public ResponseContainer(T value, ResponseStatus responseStatus) {
        mValue = value;
        mResponseStatus = responseStatus;
    }

    public T getValue() {
        return mValue;
    }

    public ResponseStatus getResponseStatus() {
        return mResponseStatus;
    }

    public boolean isSuccess() {
        return getResponseStatus().equals(ResponseStatus.SUCCESS);
    }
}
