package com.bottlerocketstudios.continuity.util;

import android.util.Log;

/**
 * Created on 8/23/16.
 */
public class SafeWait {
    private static final String TAG = SafeWait.class.getSimpleName();

    public static void safeWait(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            Log.e(TAG, "Caught java.lang.InterruptedException", e);
        }
    }
}
