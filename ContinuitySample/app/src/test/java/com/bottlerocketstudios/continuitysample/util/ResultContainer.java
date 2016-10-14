package com.bottlerocketstudios.continuitysample.util;

/**
 * Created on 9/15/16.
 */
public class ResultContainer<T> {
    private T mResult;
    private boolean mSet;

    public T getResult() {
        return mResult;
    }

    public void setResult(T result) {
        mResult = result;
        mSet = true;
    }

    public boolean isSet() {
        return mSet;
    }
}
