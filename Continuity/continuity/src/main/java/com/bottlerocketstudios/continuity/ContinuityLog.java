package com.bottlerocketstudios.continuity;


import android.util.Log;

/**
 * Created on 7/14/16.
 */
class ContinuityLog {

    private static int sMinLoggingLevel = Log.WARN;

    public static void setMinLoggingLevel(int minLoggingLevel) {
        sMinLoggingLevel = minLoggingLevel;
    }

    public static int v(String tag, String msg) {
        if (sMinLoggingLevel <= Log.VERBOSE) {
            return Log.v(tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel <= Log.VERBOSE) {
            return Log.v(tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (sMinLoggingLevel <= Log.DEBUG) {
            return Log.d(tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel <= Log.DEBUG) {
            return Log.d(tag, msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (sMinLoggingLevel <= Log.INFO) {
            return Log.i(tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel <= Log.INFO) {
            return Log.i(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (sMinLoggingLevel <= Log.WARN) {
            return Log.w(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel <= Log.WARN) {
            return Log.w(tag, msg, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (sMinLoggingLevel <= Log.ERROR) {
            return Log.e(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel <= Log.ERROR) {
            return Log.e(tag, msg, tr);
        }
        return 0;
    }

}
