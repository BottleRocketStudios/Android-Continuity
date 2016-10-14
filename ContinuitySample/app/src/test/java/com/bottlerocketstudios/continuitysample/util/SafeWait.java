package com.bottlerocketstudios.continuitysample.util;

import android.util.Log;

/**
 * Created on 8/23/16.
 */
public class SafeWait {
    private static final String TAG = SafeWait.class.getSimpleName();
    private static final long DEFAULT_INTERVAL_MS = 100;
    private static final long DEFAULT_TIMEOUT_MS = 10000;

    /**
     * Ignore interruptions. This isn't very relevant to our testing.
     */
    public static void safeWait(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            Log.e(TAG, "Caught java.lang.InterruptedException", e);
        }
    }

    public static boolean waitOnContainer(ResultContainer<?> container) {
        return waitOnContainer(container, DEFAULT_INTERVAL_MS, DEFAULT_TIMEOUT_MS);
    }

    /**
     * Perform a Thread.sleep operation until the supplied container has been set or a timeout occurs.
     * @param container     Container to check.
     * @param intervalMs    Time in ms between container checks.
     * @param timeoutMs     Time in ms before treating this as an error
     * @return  True if the value was set before timeout. False if timeout occurred.
     */
    public static boolean waitOnContainer(ResultContainer<?> container, long intervalMs, long timeoutMs) {
        long totalTimeMs = 0;
        while (!container.isSet() && totalTimeMs <= timeoutMs) {
            safeWait(intervalMs);
            totalTimeMs += intervalMs;
        }
        return container.isSet();
    }
}
