package com.bottlerocketstudios.continuity;

/**
 * Created on 8/22/16.
 */
class ContinuityContainer {
    private final Object mObject;
    private long mLifetimeMs;
    private long mExpirationMs;

    public ContinuityContainer(Object object, long lifetimeMs) {
        mObject = object;
        mLifetimeMs = lifetimeMs;
    }

    public long getLifetimeMs() {
        return mLifetimeMs;
    }

    public void updateLifetimeMs(long lifetimeMs) {
        if (mLifetimeMs < lifetimeMs) {
            mLifetimeMs = lifetimeMs;
        }
    }

    public long getExpirationMs() {
        return mExpirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        mExpirationMs = expirationMs;
    }

    public Object getObject() {
        return mObject;
    }
}
