package com.bottlerocketstudios.continuity.model;

import android.os.SystemClock;

/**
 * Created on 8/23/16.
 */
public class TestAnchor {
    private final long mCreationTimestamp;

    public TestAnchor() {
        mCreationTimestamp = SystemClock.uptimeMillis();
    }

    public long getCreationTimestamp() {
        return mCreationTimestamp;
    }
}
