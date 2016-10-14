package com.bottlerocketstudios.continuitysample.core.util;

import android.util.Log;

import com.bottlerocketstudios.continuitysample.BuildConfig;

/**
 * Created on 9/14/16.
 */
public class LogIt {

    private static boolean sStubbedMode;
    private static int sMinLoggingLevel = BuildConfig.DEBUG ? Log.VERBOSE : Log.WARN;

    public static void setStubbedMode(boolean stubbedMode) {
        sStubbedMode = stubbedMode;
    }

    public static void setMinLoggingLevel(int minLoggingLevel) {
        sMinLoggingLevel = minLoggingLevel;
    }

    public static int v(String tag, String msg) {
        if (sMinLoggingLevel > Log.VERBOSE) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg);
            return 0;
        } else {
            return Log.v(tag, msg);
        }
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel > Log.VERBOSE) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg + " " + tr.getLocalizedMessage());
            tr.printStackTrace();
            return 0;
        } else {
            return Log.v(tag, msg, tr);
        }
    }

    public static int d(String tag, String msg) {
        if (sMinLoggingLevel > Log.DEBUG) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg);
            return 0;
        } else {
            return Log.d(tag, msg);
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel > Log.DEBUG) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg + " " + tr.getLocalizedMessage());
            tr.printStackTrace();
            return 0;
        } else {
            return Log.d(tag, msg, tr);
        }
    }

    public static int i(String tag, String msg) {
        if (sMinLoggingLevel > Log.INFO) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg);
            return 0;
        } else {
            return Log.i(tag, msg);
        }
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel > Log.INFO) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg + " " + tr.getLocalizedMessage());
            tr.printStackTrace();
            return 0;
        } else {
            return Log.i(tag, msg, tr);
        }
    }

    public static int w(String tag, String msg) {
        if (sMinLoggingLevel > Log.WARN) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg);
            return 0;
        } else {
            return Log.w(tag, msg);
        }
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel > Log.WARN) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg + " " + tr.getLocalizedMessage());
            tr.printStackTrace();
            return 0;
        } else {
            return Log.w(tag, msg, tr);
        }
    }

    public static int e(String tag, String msg) {
        if (sMinLoggingLevel > Log.ERROR) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg);
            return 0;
        } else {
            return Log.e(tag, msg);
        }
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (sMinLoggingLevel > Log.ERROR) return 0;

        if (sStubbedMode) {
            System.out.println(tag + " " + msg + " " + tr.getLocalizedMessage());
            tr.printStackTrace();
            return 0;
        } else {
            return Log.e(tag, msg, tr);
        }
    }

}
