package com.chenzhipeng.mhbzdz.utils;

import android.util.Log;

public class LogUtils {

    private static String tag = "penggewudi";

    public static <T> void d(T msg) {
        if (msg != null) {
            if (msg instanceof String || msg instanceof Integer) {
                Log.d(tag, String.valueOf(msg));
            }
        } else {
            Log.d(tag, "null");
        }
    }


    public static <T> void systemOutPrint(T msg) {
        System.out.print(msg != null ? msg : "null");
    }

    public static void setTag(String s) {
        tag = s;
    }
}
