package com.bottlerocketstudios.continuity;

import android.support.annotation.NonNull;

/**
 * Created on 8/22/16.
 */
class ContinuityId implements Comparable<ContinuityId> {
    private final int mHashCode;

    public ContinuityId(String anchorId, int taskId, String continuousId, String tag) {
        //XOR hashcodes to come up with unique hash code including the tag.
        mHashCode = anchorId.hashCode() ^ taskId ^ continuousId.hashCode() ^ tag.hashCode();
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ContinuityId) {
            return other.hashCode() == mHashCode;
        }
        return super.equals(other);
    }

    @Override
    public int compareTo(@NonNull ContinuityId continuityId) {
        if (mHashCode > continuityId.hashCode()) {
            return 1;
        } else if (mHashCode < continuityId.hashCode()) {
            return -1;
        } else {
            return 0;
        }
    }
}
