package com.bottlerocketstudios.continuity.model;

import android.os.SystemClock;

import com.bottlerocketstudios.continuity.ContinuousObject;

/**
 * Created on 8/23/16.
 */
public class ContinuousTestClass implements ContinuousObject {

    private final long mCreationTimestamp;
    private boolean mDiscarded;

    public ContinuousTestClass() {
        mCreationTimestamp = SystemClock.uptimeMillis();
    }

    public long getCreationTimestamp() {
        return mCreationTimestamp;
    }

    @Override
    public void onContinuityDiscard() {
        mDiscarded = true;
    }

    public boolean isDiscarded() {
        return mDiscarded;
    }
}
