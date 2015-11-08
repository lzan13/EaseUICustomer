package com.easemob.easeui.customer.util;

import android.util.Log;

/**
 * Created by lzan13 on 2014/12/16.
 * Log 输出封装
 */
public class MLLog {

    private static String TAG_I = "melove_i";
    private static String TAG_D = "melove_d";
    private static String TAG_E = "melove_e";
    private static boolean isDebug = true;

    public static void setShowDebug(boolean b) {
        isDebug = b;
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG_I, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG_D, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG_E, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(TAG_I, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(TAG_D, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(TAG_E, msg);
        }
    }
}
