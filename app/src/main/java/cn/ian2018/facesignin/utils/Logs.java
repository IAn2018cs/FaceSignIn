package cn.ian2018.facesignin.utils;

import android.util.Log;

/**
 * 日志工具
 */

public class Logs {
    private static String TAG = "FaceSignIn TAG";
    private static int IS_OPEN = 52;

    public static void d(String msg) {
        if (IS_OPEN > 0) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (IS_OPEN > 0) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (IS_OPEN > 0) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (IS_OPEN > 0) {
            Log.v(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (IS_OPEN > 0) {
            Log.w(TAG, msg);
        }
    }
}
